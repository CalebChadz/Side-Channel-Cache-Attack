# Side-Channel-Cache-Attack
Detect the size of system LLC (Last Layer Cache) cache by timing cache reads on different lengths of data (using a side-chain algorithm to infer the size analogous to the side-chain techniques used in Spectre and Meltdown's exploitation of speculative execution). 

Reccomended parameters:
Steps = 512 or 256 for faster run time.
Test Array Size = 20
Iterations = 1
Parameter Explanation:

Steps --> Steps is the number of memory access into a test array to be performed per timed period.
It is an integer value that will be multiplied by (1024 * 1024) to give a multiple of Megabytes,
although this is truly an arbitrary number.

Test Array Size --> The size of a byte array that will be initialised. This byte array will be subject
to all the memory access. It will be an integer value multiplied by (1024 * 1024), or a multiple of
Megabytes. The larger this number is the more MB size increments of cache can be tested.

Iterations --> this refers to the number of times a times memory access into the byte array will be performed
while running this test.

Usage: 
1_$ javac SideChannelAttack.java
2_$ java SideChannelAttack <Steps> <Array Size> <Iterations>
