# 🌟 Stellar URL Shortener

![Screenshot 2025-02-17 at 9 06 14 PM](https://github.com/user-attachments/assets/7014af43-7444-4fc0-bb87-7572cd0eed27)

A lightning-fast, modern URL shortening service built with Spring Boot and Angular.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen)
![Angular](https://img.shields.io/badge/Angular-19.1-red)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green)
![License](https://img.shields.io/badge/license-MIT-blue)

## ✨ Features

- **Instant URL Shortening**: Transform long URLs into concise, shareable links
- **Custom Aliases**: Create personalized short URLs
- **Click Analytics**: Track link performance with detailed statistics
- **RESTful API**: Well-documented endpoints for seamless integration
- **Responsive Design**: Beautiful UI that works on all devices
- **Real-time Stats**: Live click count updates
- **Docker Ready**: Easy deployment with containerization

## 🚀 Tech Stack

### Backend
- **Spring Boot 3.4.2**: Modern Java framework for robust backend services
- **MongoDB**: NoSQL database for flexible data storage
- **Docker**: Containerization for consistent deployment
- **Spring Actuator**: Health monitoring and metrics
- **Lombok**: Reduced boilerplate code

### Frontend
- **Angular 19**: Powerful frontend framework
- **Material Design**: Sleek and modern UI components
- **RxJS**: Reactive programming for smooth user experience
- **TypeScript**: Type-safe development

## 🛠️ Installation

### Prerequisites
- Java 17+
- Node.js 18+
- MongoDB
- Docker (optional)

### Backend Setupbash
cd url-shortener-backend
./mvnw spring-boot:run

### Frontend Setup
bash
cd url-shortener-frontend
npm install
ng serve
Deployment
### Docker Deployment
bash
docker-compose up -d


## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/shorten` | Create short URL |
| GET | `/{shortId}` | Redirect to original URL |
| GET | `/api/stats/{shortId}` | Get URL statistics |

##  Environment Variables
env
MONGODB_URI=mongodb://urlapp:password@localhost:27017/urlshortener
PORT=8080
APP_BASE_URL=https://your-domain.com/
]

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌟 Acknowledgments

- Spring Boot team for the amazing framework
- Angular team for the powerful frontend tools
- MongoDB team for the reliable database
- All contributors who help improve this project

---

Made with ❤️  AND HARD WORK by Bernard Ginn JR
