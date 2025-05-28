# shape-overlaps
Spring Boot project to detect shape overlaps and visualize results

# 🔵 Shape Overlaps Detection System

This is a full-stack Spring Boot application that generates random 2D shapes (circles, regular polygons, and irregular polygons), detects overlaps between them using geometric algorithms, and visualizes the results in a dynamic web interface.

---

## 📌 Features

- Generates random shapes with configurable parameters
- Detects overlaps between shapes using precise geometry:
  - Circle–Circle
  - Circle–Polygon
  - Polygon–Polygon (SAT algorithm)
- Groups overlapping shapes by color
- Renders shapes on an HTML5 Canvas with IDs and colors
- Displays JSON data and shape statistics dynamically

---

## 🛠️ Tech Stack

| Layer       | Technologies Used                    |
|-------------|--------------------------------------|
| Backend     | Java 17, Spring Boot 3.2.1, Gradle   |
| Frontend    | HTML5, CSS3, JavaScript (Canvas API) |
| JSON Parser | `org.json:json`                      |
| Tools       | IntelliJ IDEA, Git, GitHub           |

---

## ⚙️ How to Run

### ✅ Prerequisites

- Java 17+
- Gradle installed or use `./gradlew` (included)
- Git (to clone the repository)

### ▶️ Running the App

```bash
# Clone the project
git clone https://github.com/YOUR_USERNAME/shape-overlaps-detection.git

cd shape-overlaps-detection

# Run the application
./gradlew bootRun

# Once the server starts, go to ---> http://localhost:8080

# Notion Page: https://www.notion.so/Shape-Overlaps-Detection-System-20049dcedb338019ae6fdf20a403aa4a?pvs=4
