db.createUser({
    user: "urlapp",
    pwd: "Berny1232",
    roles: [
        {
            role: "readWrite",
            db: "urlshortener"
        }
    ]
}); 