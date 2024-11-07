improvments:
- add users table, so that every order can be linked with an user
- allow multiple proudcts in one order
- more specific exception hanlding for different errors and validations (instead of only GlobalExceptionHandler)
- product service (inventory management service) could be a seperate project (microservice)
- notificatin service to send updates to the sellers and users 
- logging
- unit tests
- remove db connection details from code (for prod). Environment Variables or AWS KMS etc. 


host: sunkingdb.cfq6yisy4bzh.ap-south-1.rds.amazonaws.com
port: 3306
username: admin
password: arifkhan
