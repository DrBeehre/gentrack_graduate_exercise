# gentrack_graduate_exercise


Time Allocate:
1. 30 mins setting up the app, getting maven set up and importing the wanted libraries
2. 30 mins setting up basic commandline usage, reading the file in and validating
3. 1 hour reading the XML file and storing into objects
4. 1 hour printing to CSV
5. 1 hour testing and bug fixing
6. 1 hour cleaning up and commenting, adding readme

Actually time spent coding was actually pretty close to the 4 hour mark. Wasn't overly hard.

However, spent a fair bit of time looking up how to use certain libraries and thinking about how I wanted to approach this


### What was done

A java app to process the provided XML file and output two CSV's as expected

Fully unit tested. 

### What wasn't done

I would've liked to clean this up a bit, add some more logging and comments but time just got the best of me.

Some more time spent making the app more user friendly also wouldn't help

<br />

##### Something I tried and failed doing
Some thing I tried was using the JAXB library to unmarshell the XML into a bunch of objects in a few lines. <br />
The idea being, you just have to set up a bunch of object, annotate them with the right names and BOOM, everything should've fallen into place <br />
I did spend a bit of time trying this but will put that down as 'research'. <br />
The issue was the XML provided doesn't have a namespace, so the objects didn't know how to get the root element. I did spend some time trying to figure this out, but I would've had to implant a namespace into the XML throwugh the code jsut to be able to read it in and link to my objects, which I thought was a bit manky. <br />

I ended up just using the javax.xml.parsers library and just searching for tags <br />
I did still use some of the objects. I wanted to provide an app that solved the problem but also show cased how it could be used to grab any of the data if needed. Which it can kind of still do.

### What would be done with more time

As mentioned before, more logging, cleaning it all up a bit. Making the whole thing a bit more user friendly and review of how the exceptions are all handled <br />

More unit tests are never a bad thing either. Would like refactor the code to make it more private yet more testable.


