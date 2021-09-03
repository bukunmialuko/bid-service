# Bid-service

## Description

A real-time bidding agent using Scala and Akka toolkit.

## Build/Run Requirements
JDK/JRE 11 or newer is recommended to be able to build/test/run the project

## Build/Run steps
In the project dir run sbt or sbt-shell to

build: `sbt compile`

execute all unit tests: `sbt test`

sbt-revolver plugin is used to be able to start/stop the app from sbt-shell in background:

start the http server (application) in background: `sbt reStart`
stop the running akka http server: `sbt reStop`

Default http server port is configured to be **8080**.

Send POST request to `localhost:8080/check` with the following JSON body:
```json

{
    "id": "SGu1Jpq1IO",
    "site": {
        "id": "0006a522ce0f4bbbbaa6b3c38cafaa0f",
        "domain": "fake.tld"
    },
    "device": {
        "id": "440579f4b408831516ebd02f6e1c31b4",
        "geo": {
            "country": "LT"
        }
    },
    "imp": [
        {
            "id": "1",
            "wmin": 50,
            "wmax": 300,
            "hmin": 100,
            "hmax": 300,
            "h": 250,
            "w": 300,
            "bidFloor": 3.12123
        }
    ],
    "user": {
        "geo": {
            "country": "LT"
        },
        "id": "USARIO1"
    }
}

```


Server should respond with response:
```json
{
  "adId": "1",
  "banner": {
    "height": 250,
    "id": 1,
    "src": "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
    "width": 300
  },
  "bidRequestId": "SGu1Jpq1IO",
  "id": "response1",
  "price": 3.12123
}
```

