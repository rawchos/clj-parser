# clj-parser

## Description
This is a basic app that parses records based on 3 different delimited formats: pipeline, comma, and space. There's a command line component that takes a file as a parameter and then parses and displays the records in that file. The REST API component provides a way to parse a single line and display existing records based on sort order.

## Formats
  * pipe-delimited
    `LastName | FirstName | Email | FavoriteColor | DateOfBirth`
  * comma-delimited
    `LastName, FirstName, Email, FavoriteColor, DateOfBirth`
  * space-delimited
    `LastName FirstName Email FavoriteColor DateOfBirth`
