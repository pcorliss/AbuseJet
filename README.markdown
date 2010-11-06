AbuseJet
========

AbuseJet is an internal service to help web developers avoid issues with DDOS attacks, spam and heavy usage from connecting clients. It works by providing a simple configuration file and interface to memcached. The web service simply sends a HTTP GET or POST request with certain parameters and implements the suggested actions from AbuseJet. Actions are specified in the configuration file absuejet.yaml.

This service is designed to be a lightweight service separated from the rest of your architecture to ensure minimal interference, response time and uptime.

Common use cases could include

* Blocking IP addresses of known DDOS hosts
* Tarpitting automated bots
* Returning captchas to content submitters that exceed specified thresholds
* Blocking forum spam selling pharmaceuticals.

Startup
-------

You can use the following command to start AbuseJet. The default port is 8888.

	`java -jar abusejet.jar [PORT]`

Actions
-------

Actions are recomendations from the AbuseJet service. They are returned as space seprated strings in plain-text. If there are no actions to recomend the service will return 'OK'. If you have enabled it the tarpit action and status actions are handled seprately. Only tarpit and status are handled by AbuseJet, all other actions will need to be handled by your service.

####Tarpit
Actions that match 'tarpit-X' where X is an integer greater than 0 will cause the AbuseJet thread to sleep for X seconds. If you make a call to AbuseJet syncronously with your web service you will slow down the client making the request. This can be useful to slow down automated clients. Be aware that this will keep the thread handling the request open for the specified length of time and could cause issues with your infrastructure if you aren't prepared for it. This behavior can be turned off by setting the 'tarpit' configuration variable to 'false'.

####Status
Actions that match 'status-Y' where Y will cause AbuseJet to return the matching HTTP status code. For example status-404 will cause AbuseJet to return 404 error code. This behavior can be turned off by setting the 'status' configuration variable to 'false'.
  
Requirements
------------

AbuseJet requires at least one memcached server be running. This can be on the localhost. You'll need to provide appropriate monitoring of the memcached service to ensure it remains running and does not run out of memory. Careful selection of ttl values and the number of variables to track within AbuseJet can help manage this.

Admin Commands
--------------

####/admin/reload_config
Reloads the configuration from abusejet.yaml. If you've added any runtime modifications to the block list it will drop those modifications if they have not also been added to abusejet.yaml. Additionally if you modify the memcached servers configuration entry you may end up with a different hashing configuration than before which may end up resetting your usage data.

####/admin/alert_report
Displays a report of the actions that have been triggered over a period of time specified by alertsFrequency.

####/admin/clear_cache
Clears all memcached caches and alert reporting caches. Will reset all usage data.

####/admin/dump_config
Prints current running config with all comments ommited. This command does not overwrite the abusejet.yaml file. But you may incorporate it's output into the file manually.

####/admin/block
Runtime additions of new items to track and respond with actions. Takes the following arguments

* type - Parameter name to track (ex. ip, path, user-agent)
* action - Action to perform on exceeding threshold (ex. block, warn, tarpit-X, status-Y)
* ttl - Time to live or number of seconds before value resets to 0
* value - Number that will need to be exceeded before action is returned
* modifier - Specifies specfic value or regex that will need to be matched.

License
-------

AbuseJet is licensed under the GPLv3. See the LICENSE file for details.

Contact
-------

AbuseJet was created by Philip Corliss (pcorliss_at_gmail_dot_com) as part of 50projects.com. You can find more information on him and 50projects at http://blog.50projects.com 
