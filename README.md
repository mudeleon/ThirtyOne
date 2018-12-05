# ThirtyOne


App Design :
 I decided to use MVP Architecture in creating the homework so first after creating the project I usually start with creating my usual packages which compose of app, model, ui, & util. Then next is I compile/implement the libraries that I will be using on the project so after looking at requirements provided I decided to use recyclerview, cardview, glide,  realm, databinding, and MVP. I used cardview and recyclerview for displaying the inventory list  since using recyclerview is a more flexible control for handling list data and it can also reuse the cells/cardview.
 I used glide on displaying the images of pets even though it is just a placeholder in the drawable because initially i want to use API that provides random images of fruits,vegetables, and junk foods in displaying the images but after reading again the requirements it is clear that the same place holder should be use per category.
 Next, I used is realm as a database for the food object. I choose realm for its speed, ease of use, and it is well documented. Lastly, I used databinding to remove boilerplate, stronger readability and much faster the using findviewbyid. 
 For the MVP under my app is the class application where I initialized the realm and retrofit also this is where all the constants and endpoints. Next is model my model is divided into two packages the data and response since the requirements are just a simple inventory and an offline database i just have one model and doesnt have a response. Next is the ui, under ui will be all the Activity, Presenter, View, and Adapter. All the process is happening on the presenter like saving the object, updating the name, and loading the images. Under the adapter are the List of food and its cardview binding.  I also put a refresh what it will do it just simply refresh all the list and images. For the Editing and Adding of the list I just simply use custom dialog and upon saving I just call the presenter and do the saving of the updated name.
 
 
 
