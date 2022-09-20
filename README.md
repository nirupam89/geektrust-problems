# geektrust-problems

1. Command to create jar from project root folder:
 mvn clean install -DskipTests -q assembly:single
 
2. Command to run the main method from project root folder:
java -jar ./target/geektrust.jar <input_file_path>

3. Command to run unit tests:
mvn test
