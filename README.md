# bandCloud-RestAPI

Backend Spring REST-API for bandCloud

Content map under "<u>***bandcloud_api/src/main/java/com/aws_api***</u>"

| Module                         | Purpose                                                                                                | Path                                      |
|:------------------------------:|:------------------------------------------------------------------------------------------------------:|:-----------------------------------------:|
| **Models & Model Collections** | Objects used by the system                                                                             | model                                     |
| **Utilities-AWS**              | Utility package for comminly used AWS API calls, delivered as singleton                                | utils/aws                                 |
| **Utilities-Site**             | Utilities for handling dates, login/account creation, list of users and constants needed by the system | utils/site                                |
| **Controllers**                | Account and Projects controllers                                                                       | service_test/domain                       |
| DynamoUsers                    | Class making use of aws-utils and self for tailored queries to Dynamo                                  | service_testing/domain/account_controller |
| Users                          | Singleton array list of users to emulate DynamoUsers                                                   | service_testing/domain/account_controller |
