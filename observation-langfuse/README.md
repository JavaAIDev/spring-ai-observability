# Observation with Langfuse

A pair of testing API keys is created during installation. You don't need to anything if you are using this testing key pair.

If you create a new key pair, generate Langfuse HTTP Basic auth header using `$(echo -n 'PUBLIC_KEY:SECRET_KEY' | base64)`, then set to the value of `LANGFUSE_AUTH_STRING`.