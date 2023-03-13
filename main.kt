package assignment1
/*
The task at hand would be to sort students into groups.

Each student would have some kind of a score, 
and a number of groups they would like to be in. (the groups are equal in priority)

Your program should be able to generate a list of such objects.

Each group should have a limited number of potential members (< total number of students!).

Your goal is to sort students into groups based on their score 
in such a way that most students would be assigned into a group. 

If some students are left out they are grouped together for further sorting.
*/

data class Student(val name: String, val score: Float, val groups: Collection<String>) 

fun main() {
    println("Hello world!")
}
