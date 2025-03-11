# Photo Library

Photo Library is a full-stack web application that allows users to upload and manage images using Google Cloud Storage (GCS). The backend is built with Spring Boot and uses Google OAuth for authentication. The frontend is developed with React, utilizing Material-UI (MUI) for UI components and Redux for state management.

## Features

### Backend (Spring Boot)
- Java JDK 21
- Google OAuth authentication
- Image upload and retrieval using Google Cloud Storage (GCS)
- PostgreSQL database for storing image metadata
- RESTful API for frontend communication

### Frontend (React)
- Modern UI with Material-UI (MUI)
- State management with Redux
- User authentication with Google OAuth
- Image upload and display functionality

## Deployment
This application can be deployed on Google Cloud Platform (GCP) with:
- A PostgreSQL instance for metadata storage
- A VM instance for hosting the backend
- A Cloud Storage bucket for image storage
- A static website hosting service (e.g., Firebase Hosting) for the frontend

## Usage
1. Sign in with Google OAuth
2. Upload images to GCS
3. View and manage uploaded images

---

For setup and build instructions, refer to [BUILD.md](BUILD.md).

