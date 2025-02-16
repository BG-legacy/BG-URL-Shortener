db.createUser({
    user: "urlapp",
    pwd: "your_password_here",
    roles: [
        {
            role: "readWrite",
            db: "urlshortener"
        }
    ]
}); 