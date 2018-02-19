# Smart-Waste
## Usage:
+ Sending data from bin to server ~~(using `HTTP GET` request):~~ (using `HTTP POST` request)
    + ~~[https://peaceful-island-64716.herokuapp.com/iot/sensor?geo=test&status=1&pk=2](https://peaceful-island-64716.herokuapp.com/iot/sensor?geo=test&status=1&pk=2)~~
    
    +[https://peaceful-island-64716.herokuapp.com/iot/sensor](https://peaceful-island-64716.herokuapp.com/iot/sensor)
    + params:
        + `pk` is an integer representing the ID of the bin.
        + `geo` stores as a string representing the geographic location of bin#`pk`.
        + `status` stores as an integer.
            * `1`: full
            * `0`: empty
* Retrieving data from server (using `HTTP GET` request):
    - [https://peaceful-island-64716.herokuapp.com/iot/returnJSON](https://peaceful-island-64716.herokuapp.com/iot/returnJSON)
        + this URL returns a `JSON` format data containing information of all trash bins.
        * Example:
```json
[
    {"model": "bins.bin", "pk": 1, "fields": 
        {"binID": 1, "geoLocation": "testtest1", "binStatus": 3}
    }, 
    {"model": "bins.bin", "pk": 2, "fields": 
        {"binID": 2, "geoLocation": "testtest2", "binStatus": 1}
    }, 
    {"model": "bins.bin", "pk": 3, "fields": 
        {"binID": 3, "geoLocation": "testtest3", "binStatus": 4}
    }
]
```

### To-do List
+ Django Deployment checklist `python manage.py check --deploy`
+ Error handling:
    + Form validation for bin status
+ Representation of geographic locations. (currently a `Stirng`)


