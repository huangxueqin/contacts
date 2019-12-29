# contacts

The whole project consist of three modules.
I think that decomposing data source from main ui will make the project more flexible and extensible.
Also when change data source to internet or local file or Android ContentProvider, a standalone data 
source module contributes to minimum permission allocation and more solid unit test 

### api
    > defines a ContactInfo model and interfaces of contact and avatar data source
### data
    > implementation of the data source
### app
    > application ui part
