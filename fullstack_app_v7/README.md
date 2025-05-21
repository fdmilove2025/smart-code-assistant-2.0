<<<<<<< HEAD
To Change LLM:
GOTO: src/main/resources/application.properties
EDIT: spring.ai.ollama.chat.options.model="model name shown in Ollama"

To connect via Postman:
POST localhost:8080/api/code/
(return AI response as json)

POST localhost:8080/api/code/testconnect
(return your input message)

POST localhost:8080/api/code/string
(return AI response as string)

For all methods, the json RequestBody is:
{"message": "your prompt"}
=======
To Change LLM:
GOTO: src/main/resources/application.properties
EDIT: spring.ai.ollama.chat.options.model="model name shown in Ollama"

To connect via Postman:
POST localhost:8080/api/code/
(return AI response as json)

POST localhost:8080/api/code/testconnect
(return your input message)

POST localhost:8080/api/code/string
(return AI response as string)

For all methods, the json RequestBody is:
{"message": "your prompt"}
>>>>>>> 501e96912f7cb17c0adc0edd17b05eaf2cac2dfe
