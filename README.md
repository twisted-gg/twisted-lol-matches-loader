# TwistedGG - Lol Matches
Loader api

## Environment variables:
| Name                   | Description           | Example               |
|:---------------------- |:--------------------- |:--------------------  |
| APP_PORT               | Application port      | 8000                  |
| API_KEY                | Riot games api key    | XXX-XXX               |
| SUMMONERS_SERVICE      | Summoners service url | https://summoners.lol |
| MONGO_URL              | Mongo connection uri  | mongodb://{user}:{pass}@{host}:{port}/{db}?authSource=admin|
| REDIS_HOST             | Redis host            | localhost             |
| REDIS_PORT             | Redis port            | 6379                  |

## Technologies
- Kotlin
- Spring Boot 2
- Docker
- Mongo

## CI
### Get version
```gradle properties -q | grep "version:" | awk '{print $2}'```
