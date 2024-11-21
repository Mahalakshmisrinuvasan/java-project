const express = require("express");
const http = require("http");
const { Server } = require("socket.io");
const mongoose = require("mongoose");

const app = express();
const server = http.createServer(app);
const io = new Server(server);

// Connect to MongoDB
mongoose
    .connect("mongodb://localhost:27017/chatDB", { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log("Connected to MongoDB"))
    .catch((err) => console.error("MongoDB connection error:", err));

// Define a schema and model for chat messages
const messageSchema = new mongoose.Schema({
    username: String,
    content: String,
    timestamp: { type: Date, default: Date.now },
});

const Message = mongoose.model("Message", messageSchema);

// Serve a welcome message
app.get("/", (req, res) => {
    res.send("Chat server with MongoDB is running...");
});

// Handle WebSocket connections
io.on("connection", (socket) => {
    console.log("A user connected");

    // Send previous messages to the newly connected user
    Message.find()
        .sort({ timestamp: 1 })
        .then((messages) => {
            socket.emit("previous messages", messages);
        })
        .catch((err) => console.error("Error fetching messages:", err));

    // Listen for new chat messages
    socket.on("chat message", (msg) => {
        // Save the message to the database
        const newMessage = new Message({ username: "User", content: msg });
        newMessage
            .save()
            .then(() => {
                // Broadcast the message to all connected clients
                io.emit("chat message", newMessage);
            })
            .catch((err) => console.error("Error saving message:", err));
    });

    socket.on("disconnect", () => {
        console.log("A user disconnected");
    });
});

// Start the server
server.listen(3000, () => {
    console.log("Server running on http://localhost:3000");
});
mongoose.connect("mongodb://localhost:27017/chatDB", { useNewUrlParser: true, useUnifiedTopology: true });