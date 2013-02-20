# Dot_AST_Viewer

This is my custom Visitor I wrote to better visualize the ASTs we're generating in Comp 520.

It consists of two components:
  1. **DotASTDisplay.java**
  2. **Node.java**

Place these components in your miniJava package along with your Compiler.java.

**DotASTDisplay.java** is the actual visitor that will traverse your AST and output to stdout your AST in .dot format.

.dot format is the format Graphviz uses to make nice graphs. (see www.graphviz.org/doc/info/lang.html)

**Before continuing, make sure you have Graphviz installed (see www.graphviz.org/).**

## DOT files

After running your compiler using DotASTDisplay, you'll want to copy the output into a text editor and save it with a '.dot' file format. 

To produce a PNG image from 'yourNamedFile.dot' file, issue the following command:

    dot -Tpng models.dot > models.png

## Options

DotASTDisplay.java can output graphs with two different options:
  1. Cluster Binary Expressions together (essentially draw boxes around them for easier viewing)
  2. Color terminals and names

To enable these options, DotASTDisplay.java takes 1 to 2 arguments in it's constructor as follows:

```java
// Clusters for BinExpr and Colors
public DotASTDisplay(boolean clusterBinExpr, boolean colors) {...

// Clusters for BinExpr only
public DotASTDisplay(boolean clusterBinExpr) {...
```

The default  constructor works as well, but with no options used.

```java
// No options
public DotASTDisplay() {}
```
