# OntoRetrieval

##Introduction

The purpose of this project is to extract facts/triplets from unstructured data (text) and to model the graph based on the retrieval triplets and a set of the applicable algorithms for extracted data. This data structure is very similar to our real world, where almost each entity is connected to another entity(s). The connections (relations) between the entities are also very important information: it could be used for building 'grammar' to manipulate/search by/through such data structure.
On the top level, the representation of all entities will have ontological format (e.g., entity 'Moscow' has ontological format like </en/.../settlement/city/Moscow>). This format has a wide range of usage: unique entity identification, modeling flexible metrics for data manipulation, automatic machine translation and so on.

##Dependent packages

To build this, you need to download [DKpro](https://dkpro.github.io/).
 
##Limitations

Current implementation handles only English language. In future it will handle Russian language as well. When the support of these 2 languages will be implemented, there will be a 'bridge' which will combine all languages models for all implemented algorithms (multilingual search without linkage to a particular language).
 
##Algorithms for data manipulation

The main purpose of all algorithms is the multilingual Question-Answer system.

* Search for the simple answers:

> Q: What is a pen?

> Q: Who are invented a pen?

> Q: What is the biggest city in the world?

* Search for answer for the deep questions:

> Q: What is the densest settlement region in the Moscow?

> Q: What is the set of the cities in the middle Europe which we infected by 'Black Death' in the 14th century?

* Search for the answers which require comparison actions on the existing graph (the case when the the apparent data for answer is absent):

> Q: Who is the bigger, an elephant or a 'pygmy right whale'?

> Q: Which city has the highest average year temperature, Moscow or Nizhny Novgorod?
 
##Implemented features

* Extraction of facts from unstructured text data (English language).
* Co-reference Resolver.
* Added Triplets object to UIMA indexer.
* Utils for generate Neo4j script for modeling sentence semantic structure.
 
All of existing feature will be improved in future. If you see: incorrect fact extraction or think it could be improved in some specific case, plz create a githib issue.
