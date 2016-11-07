# Feedback Vertex Set  

### De quoi s'agit-il ?
Un 'feedback vertex set' (FVS) d'un graphe est un sous ensemble de sommets qui contient au moins un sommet de chaque cycle dans le graphe. Le but est donc de trouver un 'feedback vertex set' de taille minimum. Nous travaillons sur un graphe non-orienté. Cette page présente une implémentation de l' algorithme Bafna-Berman-Fujito. Le langage Java est utilisé.

<img src="https://www.dropbox.com/s/7a87jm0ppd96zsv/notvalidfvs.svg?dl=1" width="280">
<img src="https://www.dropbox.com/s/0sj4ccks7xfmba2/validfvs2.svg?dl=1" width="280">
<img src="https://www.dropbox.com/s/xcvi0lwwizoi98m/validfvs.svg?dl=1" width="280">


### Execution
Le projet contient un __build.xml__ avec les commandes suivantes :
- __compile__ : compilation de l'interface de test et de l'algorithme
- __run__ : lance l'interface de test de l'algorithme

### Implementation
Nous expliquons ici la structure de l'implémentation ainsi que des choix techniques effectués (structure de données..)

#### Classe DefaultTeam
Cette class contient l'algorithme principal ainsi que les différentes méthodes nécessaires à son implémentation.

```java
public ArrayList<Point> calculFVS( ArrayList<Point> points )
```
Méthode appelée par le GUI de test.

__Parametres__ :
- __points__ : les sommets du graphe

__Retour__ :
Un minimum feedback vertex set du graphe.

---
 
```java
public ArrayList<Point> bbfAlgo( ArrayList<Point> points )
```
Implémentation de l'algorithme tel que décrit dans [l'article](https://www-apr.lip6.fr/~buixuan/files/aaga2015/FVS/vfs_bafna.pdf)

Une __HashMap__ est utilisée pour associer un poids à un sommet. Ainsi l'obtention du poids d'un sommet se fait en **0(1)**. La structure de donnée __HashSet__ de java est utilisée pour représenter des ensembles de sommets ( par exemple pour l'ensemble __F__). Cette structure de donnée offre des opérations d'ajout, suppression et d'__appartenance__ en __temps constant__. De cette façon nous pouvons facilement tester l'appartenance d'un point particulier à cet ensemble en O(1).

__Parametres :__

- __points__ : les sommets du graphe

__Retour :__
Un minimum feedback vertex set du graphe.

---

```java
public double min( ArrayList<Point> v , Map<Point , Double> weight )
```
__Parametres :__
- __v__ : les sommets du graphe
- __weight__ : les poids des sommets du graphe

__Retour :__
min{weight(u) : u∈v}

---

```java
public double min2( ArrayList<Point> v , Map<Point , Double> weight )
```
__Parametres :__

- __v__ : les sommets du graphe
- __weight__ : les poids des sommets du graphe

__Retour :__
min{weight(u)/(d(u)−1) | u∈v}

---

```java
public int d( Point p , ArrayList<Point> vertices )
```
Permet d'obtenir le degré d'un sommet du graphe.

__Parametres :__
- __p__ : le sommet du graphe
- __vertices__ : les sommets du graphe
__Retour :__
le degré de p dans vertices

---


```java
public void cleanUp( ArrayList<Point> v )
```
Suppression de tous les sommets du graphe de degrés au plus 1. Correspond à la procedure __Cleanup__ de [l'article](https://www-apr.lip6.fr/~buixuan/files/aaga2015/FVS/vfs_bafna.pdf)

__Parametres :__

- __v__ : les sommets du graphe


---

```java
public ArrayList<Point> semiDisjoint( ArrayList<Point> vertices )
```
Cherche un cycle semidisjoint C dans le graphe. C est semidisjoint si, pour chaque sommet u de C, d(u) = 2, avec au plus 1 exception.
Etapes de la fonction :
- recherche d'un cycle dans le graphe
- Analyse du degré des sommets du cycle (si cycle il y a)

__Parametres :__

- __vertices__ : les sommets du graphe

__Retour :__
Un cycle semidisjoint s'il existe, null sinon

---

```java
public ArrayList<Point> getCycle( ArrayList<Point> vertices ) 
```
Cherche un cycle dans le graphe. C'est un algorithme de parcours en profondeur récursif.
- Un __HashSet__ est utilisé pour marquer les sommets visités. (test d'appartenance aux sommets visités effectué en temps constant grace à cette structure de données).
- Une pile __Deque__ est utilisé pour 'sortir' le cycle trouvé à la fin de l'algorithme.

__Parametres :__

- __vertices__ : les sommets du graphe

__Retour :__
Un cycle

--- 

```java
private boolean dfs( ArrayList<Point> g , Point v , Point father , Set<Point> visited , Deque<Point> stack ) 
```

Fonction appelée récursivement pour le parcours en profondeur du graphe.

__Parametres :__
- __g__ : les sommets du graphe
- __v__ : le sommet courant
- __father__ : le dernier père visité du sommet courant
- __visited__ : l'ensemble des sommets visités
- __stack__ : contient le cycle à la fin de l'execution de l'algorithme s'il existe

__Retour :__
Vrai si un cycle est détecté, faux sinon

#### Classe Evaluation
Cette classe utilitaire contient des fonctions permettant d'évaluer la validité du FVS produit par l'algorithme.

```java
private static boolean isMember( ArrayList<Point> points , Point p )
```
Permet de déterminer si un point p appartient à une liste de points

__Parametres :__

- __p__ : le point à cherche
- __points__ : l'ensemble de points à chercher

__Retour :__
Vrai si p appartient à points, faux sinon

---
```java
public static boolean isValide( ArrayList<Point> origPoints , ArrayList<Point> fvs )
```
Permet de tester si fvs est un fvs dans origPoints

__Parametres :__
- __origPoints__ : l'ensemble de points
- __fvs__ : le fvs à tester

__Retour :__
Vrai si fvs est un minimum feedback vertex set dans origPoints.

---
```java
public static ArrayList<Point> neighbor( Point p , ArrayList<Point> vertices )
```
Permet d'obtenir les voisins de p dans le graphe de vertices

__Parametres :__

- __p__ : le point dont on souhaite obtenir les voisins
- __vertices__ : les sommets du graphe

__Retour :__
Une liste de points qui sont les voisins de p

### Exemples
Voici le résultat (à droite) de l'algorithme appliqué sur un graphe (à gauche).

<img src="https://cloud.githubusercontent.com/assets/7091110/20041373/369c2662-a468-11e6-8b0e-1fdf18c0bea1.png" width="300">
<img src="https://cloud.githubusercontent.com/assets/7091110/20041372/3688a240-a468-11e6-8717-d1214ef22f7f.png" width="300">


### Historique de développement
Versions | Description
---------|------------
1.0      | Implémentation de l'algorithme Bafna-Berman-Fujito

### Références
- Article de recherche de l'algorithme : https://www-apr.lip6.fr/~buixuan/files/aaga2015/FVS/vfs_bafna.pdf

