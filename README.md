# Smart-Waste
## Usage:
### Sending data from bin to server ~~(using `HTTP GET` request):~~ (using `HTTP POST` request)
+ ~~[https://peaceful-island-64716.herokuapp.com/iot/sensor?geo=test&status=1&pk=2](https://peaceful-island-64716.herokuapp.com/iot/sensor?geo=test&status=1&pk=2)~~
    
+[https://peaceful-island-64716.herokuapp.com/iot/sensor](https://peaceful-island-64716.herokuapp.com/iot/sensor)
+ params:
+ `pk` is an integer representing the ID of the bin.
+ `geo` stores as a string representing the geographic location of bin#`pk`.
+ `status` stores as an integer.
	* `1`: full
	* `0`: empty
### Retrieving data from server (using `HTTP GET` request):
- [https://peaceful-island-64716.herokuapp.com/iot/returnJSON](https://peaceful-island-64716.herokuapp.com/iot/returnJSON)
	+ this URL returns a `JSON` format data containing information of all trash bins.
	* Example:
```json
[
    {
        "model": "bins.bin",
        "pk": 1, 
        "fields": 
        {
            "binID": 2, 
            "geoLocation": "40.442877, -79.943236", 
            "binStatus": 0, 
            "requestUrgent": 0, 
            "lastPickUpTime": "11:09PM on March 25, 2018"
        }
    }
]
```

+ `binID`: ID of the trash bin
+ `geoLocation`: latitude and longitude (`lat,lon`)
+ `binStatus`:
    * `0`: empty
    * `1`: full
* `requestUrgent`:
    - `0`: `true`
    - `1`: `false`
- `lastPickUpTime`:
    + time in the format: `11:09PM on March 25, 2018`
    + default: `None`
### Sending a damage report (using `HTTP POST` request)
+ [https://peaceful-island-64716.herokuapp.com/iot/reportDamage](https://peaceful-island-64716.herokuapp.com/iot/reportDamage)
+ params:
    + `geoLocation` stores as a string representing the geographic location of bin#`pk`.
        + format: `(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)`
    + `description`: a short description for the damage
    + `binID`: an integer representing the ID of the bin.


### Getting all damage reports
- [https://peaceful-island-64716.herokuapp.com/iot/getAllDamageReports](https://peaceful-island-64716.herokuapp.com/iot/getAllDamageReports)

- Sample response
```json
[
	{
		"model": "bins.damagereportmodel", 
		"pk": 1, 
		"fields": 
		{
			"binID": 5, 
			"geoLocation": "40.451839, -79.928417", 
			"description": "This is a sample damage report description.", 
			"status": 0
		}
	}, 
	{
		"model": "bins.damagereportmodel", 
		"pk": 2, 
		"fields": 
		{
			"binID": 2, 
			"geoLocation": "40.441432, -79.955414", 
			"description": "It's broken.", 
			"status": 0
		}
	}
]
```
    
