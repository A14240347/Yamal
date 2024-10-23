
#!/bin/bash

# Check if API_KEY is set
if [ -z "$API_KEY" ]; then
    echo "Error: No API key provided. Please set the API_KEY environment variable."
    exit 1
fi

# Set default model if not provided
MODEL=${2:-"text-davinci-003"}

# Make the API call to OpenAI
response=$(curl -s https://api.openai.com/v1/completions   -H "Content-Type: application/json"   -H "Authorization: Bearer $API_KEY"   -d "{
        \"model\": \"$MODEL\",
        \"prompt\": \"$1\",
        \"max_tokens\": 150
      }")

# Check if the curl command was successful
if [ $? -ne 0 ]; then
    echo "Error en la llamada a la API de OpenAI. Verifica tu conexión y tu clave de API."
    exit 1
fi

# Print the response
echo "Respuesta de OpenAI: $response"
