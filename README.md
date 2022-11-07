# SpringBoot_URL_Shortener


A HTTP service that serves to shorten URLs, with the following functionalities:




Registration Web address
Redirect client in accordance with the shortened URL
Usage statistics
The app is implemented using Spring Boot, JPA and a H2 (in-memory) db.
Create account
To create account, make request to this uri 'http://127.0.0.1:8080/account' with POST method and following parameters in JSON format

"AccountId": "{accountId}"

'accountId' is a String parameter, and is required

In case {accountId} is new, password is generated and returned



URLs registration
To register URL, you need to make request to this uri 'http://127.0.0.1:8080/register' with POST method and following parameters in JSON format

"url": "{url}"

"redirectType": "{redirectType}"

'url' is a String parameter, and is required

'redirectType' is a Integer parameter, and is optional (301 is default)

You also need to fill Authorization header with accountId and password, which was generated in first step

In case authorization is valid, url and redirect type is returned

Statistics
To retrieve statistic, make request to this uri 'http://127.0.0.1:8080/statistic/{accountId}' with GET method

'accountId' is a String parameter, and is required

'accountId' is string of account which you want to get statistic

You also need to fill Authorization header with accountId and password, which was generated in first step

In case authorization is valid, statistic is returned



The jar executable can be found in the root of the project, or within the target dir.
- All dependencies and config. are included/is done within the project itself, and doesn't require any external configuration.
