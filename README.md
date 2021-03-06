# ESILV_Mobile_Apps_Project


## Question

### How you ensure user is the right one starting the app?

I ensure user is the right one starting the app with an authentification view, which impose to the user to set a password if it's first utilisation or use his password to access the other view of his application.

### How do you securely save user's data on your phone ?

I save user's data by using Room Database. Room store it's sqlite file in app's internal data directory so no other app can access, you need to be root user to access data.

### How did you hide the API url ?

To hide the API url, I have choose to use C++ JNI native code to hide sensitive string data with NDK and CMake. Because C++ clases are stored inside generated .so files which are very hard to decompile

## Screenshot

![Alt text]( src/screenshot/auth.png?raw=true "Title")
![Alt text]( src/screenshot/vue.png?raw=true "Title")
![Alt text]( src/screenshot/form.png?raw=true "Title")



### Create account function
Their is a weird things when I tried to create account on the API. First I try with PostMan and it works we have the right to create new account. But when I have tried with my android application the json sent seems correct and something is added in the accounts, but it's not what I have sent.

