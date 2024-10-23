
# Proyecto Chatbot Avanzado

Este proyecto incluye un chatbot avanzado que utiliza procesamiento de lenguaje natural (NLP) para interactuar con usuarios a través de voz y texto, además de flujos de trabajo automatizados con CI/CD.

## Requisitos

- **Java 11**
- **Gradle**
- **Python 3.8 o superior**
- **Dart SDK**
- **Claves API**: Necesitarás claves de OpenAI y Google Cloud para habilitar el reconocimiento de voz y procesamiento de texto.

## Instalación

### 1. Instalación de dependencias

#### Python
Instala las dependencias de Python usando el archivo `requirements.txt`:

```bash
pip install -r requirements.txt
```

#### Dart
Ejecuta el script para instalar Dart según tu sistema operativo:

```bash
./instalar_dart.sh
```

### 2. Configuración de claves API

Configura las claves API para OpenAI y Google Cloud:

```bash
export OPENAI_API_KEY="tu_clave_openai_aqui"
export GOOGLE_CLOUD_API_KEY="tu_clave_google_cloud_aqui"
```

### 3. Ejecución del Chatbot

Puedes ejecutar el script del asistente para interactuar con el chatbot:

```bash
./gpt_assistant.sh "Tu mensaje aquí"
```

### 4. Flujo de trabajo CI/CD

El proyecto incluye un pipeline de CI/CD basado en **GitHub Actions** que se activa automáticamente en cada `push` o `pull request`. Esto compila la aplicación Android, ejecuta pruebas y despliega el proyecto.

### 5. Despliegue

El despliegue de la aplicación se realiza en **Firebase**, y se puede automatizar mediante el archivo `pusbec.yaml`.

## Contribuciones

Las contribuciones son bienvenidas. Asegúrate de seguir las mejores prácticas y realizar pruebas antes de enviar un PR.
