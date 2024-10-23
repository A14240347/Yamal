
#!/bin/bash

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    if command -v apt-get &> /dev/null; then
        echo "Instalando Dart con apt-get..."
        sudo apt-get update && sudo apt-get install dart
    elif command -v yum &> /dev/null; then
        echo "Instalando Dart con yum..."
        sudo yum install dart
    fi
elif [[ "$OSTYPE" == "darwin"* ]]; then
    if ! command -v brew &> /dev/null; then
        echo "Instalando Homebrew..."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    fi
    echo "Instalando Dart con Homebrew..."
    brew tap dart-lang/dart
    brew install dart
elif [[ "$OSTYPE" == "msys"* ]]; then
    echo "Windows detectado. Por favor, sigue las instrucciones de instalación manual de Dart en: https://dart.dev/get-dart"
else
    echo "Sistema operativo no soportado. Por favor, instala Dart manualmente."
    exit 1
fi
