# vote_system
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.

<b>
AdminRestController<br>
</b>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/admin<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/admin/102<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/admin/result<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/admin?localtime=12:00:00<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/admin/by?email=user2@yandex.ru<br>
curl -u admin@gmail.com:admin -X DELETE http://localhost:8080/vote_system/rest/admin/102<br>
curl -u admin@gmail.com:admin -d '{"name":"UserNew","last_name":"Userovich","email":"user3@yandex.ru", 
"password":"pass1"}' -H 'Content-Type: application/json' -X POST http://localhost:8080/vote_system/rest/admin<br><br>
curl -u admin@gmail.com:admin -d '{"id":114,"name":"UserUser","last_name":"Userovich","email":"user3@yandex.ru", 
"password":"pass1", "enabled":true,"registered":"2020-01-11T20:28:10.358+0000","roles":["ROLE_USER"]}' 
-H 'Content-Type: application/json' -X PUT http://localhost:8080/vote_system/rest/admin/114<br><br>
curl -u admin@gmail.com:admin -X PATCH http://localhost:8080/vote_system/rest/admin/102?enabled=false<br>
<br>
<b>
DishRestController<br>
</b>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/dishes<br>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/dishes/105<br>
curl -u admin@gmail.com:admin -X DELETE http://localhost:8080/vote_system/rest/dishes/105<br>
curl -u admin@gmail.com:admin -d '{"dish_name":"Sopa De Pollo","price":50.0,"rest_id":103}' 
-H 'Content-Type: application/json' -X POST http://localhost:8080/vote_system/rest/dishes<br><br>
curl -u admin@gmail.com:admin -d '{"id":114,"dish_name":"Sopa De Pollo Lux","price":60.0,"rest_id":103}' 
-H 'Content-Type: application/json' -X PUT http://localhost:8080/vote_system/rest/dishes/114<br>
<br>
<b>
RestaurantRestController<br>
</b>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/restaurants<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/restaurants/total<br>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/restaurants/103<br>
curl -u admin@gmail.com:admin -X DELETE http://localhost:8080/vote_system/rest/restaurants/103<br>
curl -u admin@gmail.com:admin -d '{"name":"Belkin2"}' -H 'Content-Type: application/json' 
-X POST http://localhost:8080/vote_system/rest/restaurants<br><br>
curl -u admin@gmail.com:admin -d '{"dish_name":"Sopa De Pollo","price":50.0,"rest_id":114}' 
-H 'Content-Type: application/json' -X POST http://localhost:8080/vote_system/rest/dishes<br><br>
curl -u admin@gmail.com:admin -d '{"id":114,"name":"Belkin2","dishList":
[{"id":115,"dish_name":"Sopa De Pollo","price":50.0,"rest_id":116}]}' -H 'Content-Type: application/json' 
-X PUT http://localhost:8080/vote_system/rest/restaurants/114<br><br>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/restaurants/114<br>
<br>
<b>
VoteRestController<br>
</b>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/votes<br>
curl -u admin@gmail.com:admin http://localhost:8080/vote_system/rest/votes/total<br>
curl -u user@yandex.ru:password http://localhost:8080/vote_system/rest/votes/111<br>
curl -u admin@gmail.com:admin -X DELETE http://localhost:8080/vote_system/rest/votes/111<br>
curl -u user@yandex.ru:password -d '{"rest_id":103}' -H 'Content-Type: application/json' 
-X POST http://localhost:8080/vote_system/rest/votes<br><br>
curl -u user@yandex.ru:password -d '{"id":114,"user_id":100,"rest_id":103}' 
-H 'Content-Type: application/json' -X PUT http://localhost:8080/vote_system/rest/votes/114<br>
