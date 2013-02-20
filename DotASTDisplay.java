package miniJava;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

/**
 * Display AST in DOT format
 * @author Blaine Stancill
 * @email blaine.stancill@gmail.com
 * @version COMP 520 V2.3
 * 
 */
public class DotASTDisplay implements Visitor<Node,Node> {
	private boolean clusters = false;
	private String terminalColor = "lightgrey";
	private String nameColor = "lightblue";

	/**
	 * 
	 * @param clusterBinExpr - Cluster Binary Expressions in boxes
	 * 							for easy viewing
	 * @param colors - Display colors?
	 */
	public DotASTDisplay(boolean clusterBinExpr, boolean colors) {
		this.clusters = clusterBinExpr;		
		Node.color = colors;
	}

	/**
	 * 
	 * @param clusterBinExpr - Cluster Binary Expressions in boxes
	 * 							for easy viewing
	 */
	public DotASTDisplay(boolean clusterBinExpr) {
		this.clusters = clusterBinExpr;
	}

	public DotASTDisplay() {}

	public void showTree(AST ast){
		// Setup default graph settings...
		System.out.println("digraph G {");
		System.out.println("ratio=auto; \ncenter=true; \nranksep=equally;");
		System.out.println("margin=0;\n" +
				"ratio=compress;\n" +
				"center=true;\n" +
				"ranksep=\"0.5 equally\";\n" +
				"nodesep=1;\n" +
				"remincross=true;\n" +
				"node [fontsize=10, fontname=\"Times-Roman\", shape=ellipse];\n ");
		ast.visit(this, null);
		System.out.println("}");
	}

// Package
	@Override
	public Node visitPackage(Package prog, Node arg) { 
		Node pack = new Node(prog.toString());
		pack.setNodeStyle("", "peripheries=2").print();		

		Node classDeclList = new Node(pack, "ClassDeclList");
		classDeclList.addToLabelName("\\[" + prog.classDeclList.size() + "\\]").print();

		for (ClassDecl c: prog.classDeclList){
			c.visit(this, classDeclList);
		}
		return pack;
	}

// Declarations
	@Override
	public Node visitClassDecl(ClassDecl cd, Node parent) {
		Node classDecl = new Node(parent, cd.toString());
		classDecl.print();

		Node name = new Node(classDecl, "Name");
		name.setNodeLabel("\\\"" + cd.name + "\\\"").setNodeStyle(nameColor).print();

		Node fieldDeclList = new Node(classDecl, "FieldDeclList");
		fieldDeclList.addToLabelName("\\[" + cd.fieldDeclList.size() + "\\]").print();;
		for (FieldDecl f: cd.fieldDeclList)
			f.visit(this, fieldDeclList);

		Node methodDeclList = new Node(classDecl, "MethodDeclList");
		methodDeclList.addToLabelName("\\[" + cd.methodDeclList.size() + "\\]").print();
		for (MethodDecl m: cd.methodDeclList)
			m.visit(this, methodDeclList);
		return classDecl;
	}

	@Override
	public Node visitFieldDecl(FieldDecl fd, Node parent) {
		Node fieldDecl = new Node(parent, fd.toString());
		fieldDecl.setNodeLabel("(" + (fd.isPrivate ? "private": "public")
				+ (fd.isStatic ? " static) " :") ") + fd.toString()).print();

		Node type = fd.type.visit(this, fieldDecl);

		Node name = new Node(type, "Name");
		name.setNodeLabel("\\\"" + fd.name + "\\\"").setNodeStyle(nameColor).print();

		return fieldDecl;
	}

	@Override
	public Node visitMethodDecl(MethodDecl md, Node parent) {
		Node methodDecl = new Node(parent, md.toString());
		methodDecl.setNodeLabel("(" + (md.isPrivate ? "private": "public") 
				+ (md.isStatic ? " static) " :") ") + md.toString()).print();

		Node type = md.type.visit(this, methodDecl);

		Node name = new Node(type, "Name");
		name.setNodeLabel("\\\"" + md.name + "\\\"").setNodeStyle(nameColor).print();

		ParameterDeclList pdl = md.parameterDeclList;
		Node parameterDeclList = new Node(methodDecl, "ParameterDeclList");
		parameterDeclList.addToLabelName("\\[" + pdl.size() + "\\]").print();
		for (ParameterDecl pd: pdl) {
			pd.visit(this, parameterDeclList);
		}

		StatementList sl = md.statementList;
		Node statementList = new Node(methodDecl, "StmtList");
		statementList.addToLabelName("\\[" + sl.size() + "\\]").print();
		for (Statement s: sl) {
			s.visit(this, statementList);
		}

		if (md.returnExp != null) {
			md.returnExp.visit(this, methodDecl);
		}
		return methodDecl;
	}

	@Override
	public Node visitParameterDecl(ParameterDecl pd, Node parent) {
		Node parameterDecl = new Node(parent, pd.toString());
		parameterDecl.print();

		Node type = pd.type.visit(this, parameterDecl);

		Node name = new Node(type, "Name");
		name.setNodeLabel("\\\"" + pd.name + "\\\"").setNodeStyle(nameColor).print();

		return parameterDecl;
	}

	@Override
	public Node visitVarDecl(VarDecl decl, Node parent) {
		Node varDecl = new Node(parent, decl.toString());
		varDecl.print();

		Node type = decl.type.visit(this, varDecl);

		Node name = new Node(type, "Name");
		name.setNodeLabel("\\\"" + decl.name + "\\\"").setNodeStyle(nameColor).print();

		return varDecl;
	}

// Types
	@Override
	public Node visitBaseType(BaseType type, Node parent) {
		Node baseType = new Node(parent, type.toString());
		baseType.setNodeLabel(type.typeKind + " " + type.toString()).print();
		return baseType;
	}

	@Override
	public Node visitClassType(ClassType type, Node parent) {
		Node classType = new Node(parent, type.toString());
		classType.print();

		Node name = new Node(classType, "Name");
		name.setNodeLabel("\\\"" + type.className + "\\\"").setNodeStyle(nameColor).print();

		return classType;
	}

	@Override
	public Node visitArrayType(ArrayType type, Node parent) {
		Node arrayType = new Node(parent, type.toString());
		arrayType.print();

		type.eltType.visit(this, arrayType);
		return arrayType;
	}

// Statements
	@Override
	public Node visitBlockStmt(BlockStmt stmt, Node parent) {
		Node blockStmt = new Node(parent, stmt.toString());
		blockStmt.print();

		StatementList sl = stmt.sl;
		Node statementList = new Node(blockStmt, "StatementList");
		statementList.addToLabelName("\\[" + sl.size() + "\\]").print();
		for (Statement s: sl) {
			s.visit(this, statementList);
		}
		return blockStmt;
	}

	@Override
	public Node visitVardeclStmt(VarDeclStmt stmt, Node parent) {
		Node varDeclStmt = new Node(parent, stmt.toString());
		varDeclStmt.print();

		stmt.varDecl.visit(this, varDeclStmt);	
		if (stmt.initExp != null)
			stmt.initExp.visit(this, varDeclStmt);
		return varDeclStmt;
	}

	@Override
	public Node visitAssignStmt(AssignStmt stmt, Node parent) {
		Node assignStmt = new Node(parent, stmt.toString());
		assignStmt.print();

		stmt.ref.visit(this, assignStmt);
		stmt.val.visit(this, assignStmt);
		return assignStmt;
	}

	@Override
	public Node visitCallStmt(CallStmt stmt, Node parent) {
		Node callStmt = new Node(parent, stmt.toString());
		callStmt.print();

		stmt.methodRef.visit(this, callStmt);

		ExprList al = stmt.argList;
		Node expList = new Node(callStmt, "ExprList");
		expList.addToLabelName("\\[" + al.size() + "\\]").print();
		for (Expression e: al) {
			e.visit(this, expList);
		}
		return callStmt;
	}

	@Override
	public Node visitIfStmt(IfStmt stmt, Node parent) {
		Node ifStmt = new Node(parent, stmt.toString());
		ifStmt.print();

		stmt.cond.visit(this, ifStmt);
		stmt.thenStmt.visit(this, ifStmt);
		if (stmt.elseStmt != null)
			stmt.elseStmt.visit(this, ifStmt);
		return ifStmt;
	}

	@Override
	public Node visitWhileStmt(WhileStmt stmt, Node parent) {
		Node whileStmt = new Node(parent, stmt.toString());
		whileStmt.print();

		stmt.cond.visit(this, whileStmt);
		stmt.body.visit(this, whileStmt);
		return whileStmt;
	}

// Expressions
	@Override
	public Node visitUnaryExpr(UnaryExpr expr, Node parent) {
		Node unaryExp = new Node(parent, expr.toString());
		unaryExp.print();

		expr.operator.visit(this, unaryExp);
		expr.expr.visit(this, unaryExp);
		return unaryExp;
	}

	@Override
	public Node visitBinaryExpr(BinaryExpr expr, Node parent) {
		Node binaryExp = new Node(parent, expr.toString());
		binaryExp.print();

		if (clusters)
			System.out.println("subgraph cluster" + Node.getId() + "{");

		expr.operator.visit(this, binaryExp);
		expr.left.visit(this, binaryExp);
		expr.right.visit(this, binaryExp);

		if (clusters)
			System.out.println("}");
		return binaryExp;
	}

	@Override
	public Node visitRefExpr(RefExpr expr, Node parent) {
		Node refExpr = new Node(parent, expr.toString());
		refExpr.print();

		expr.ref.visit(this, refExpr);
		return refExpr;
	}

	@Override
	public Node visitCallExpr(CallExpr expr, Node parent) {
		Node callExpr = new Node(parent, expr.toString());
		callExpr.print();

		expr.functionRef.visit(this, callExpr);

		ExprList al = expr.argList;
		Node exprList = new Node(callExpr, "ExprList");
		exprList.addToLabelName("\\[" + al.size() + "\\]").print();
		for (int i = 0; i < al.size(); i++) {
			al.get(i).visit(this, exprList);
		}
		return callExpr;
	}

	@Override
	public Node visitLiteralExpr(LiteralExpr expr, Node parent) {
		Node literalExpr = new Node(parent, expr.toString());
		literalExpr.print();

		expr.literal.visit(this, literalExpr);
		return literalExpr;
	}

	@Override
	public Node visitNewObjectExpr(NewObjectExpr expr, Node parent) {
		Node newObjectExpr = new Node(parent, expr.toString());
		newObjectExpr.print();

		expr.classtype.visit(this, newObjectExpr);
		return newObjectExpr;
	}

	@Override
	public Node visitNewArrayExpr(NewArrayExpr expr, Node parent) {
		Node newArrayExpr = new Node(parent, expr.toString());
		newArrayExpr.print();

		expr.eltType.visit(this, newArrayExpr);
		expr.sizeExpr.visit(this, newArrayExpr);
		return newArrayExpr;
	}

// References
	@Override
	public Node visitQualifiedRef(QualifiedRef qr, Node parent) {
		Node qualifiedRef = new Node(parent, qr.toString());
		qualifiedRef.print();

		Node thisNode = null;
		if (qr.thisRelative) {
			thisNode = new Node(qualifiedRef, "this");
			thisNode.setNodeLabel("\\\"" + "this" + "\\\"").setNodeStyle(terminalColor).print();
		}

		IdentifierList ql = qr.qualifierList;
		Node startQualiNode = null;
		if (ql.size() > 0) {
			if (thisNode == null) {
				startQualiNode = ql.get(0).visit(this, qualifiedRef);
			} else {
				startQualiNode = ql.get(0).visit(this, thisNode);
			}

			Node tempNode = null;
			for (int i = 1; i < ql.size(); i++) {
				if (i == 1)
					tempNode = ql.get(i).visit(this, startQualiNode);
				else
					tempNode = ql.get(i).visit(this, tempNode);
			}
		}
		return qualifiedRef;
	}

	@Override
	public Node visitIndexedRef(IndexedRef ir, Node parent) {
		Node indexedRef = new Node(parent, ir.toString());
		indexedRef.print();

		ir.ref.visit(this, indexedRef);
		ir.indexExpr.visit(this, indexedRef);
		return indexedRef;
	}

// Terminals
	@Override
	public Node visitIdentifier(Identifier id, Node parent) {
		Node identifier = new Node(parent, id.toString());
		identifier.setNodeLabel("\\\"" + id.spelling + "\\\" " + id.toString()).setNodeStyle(terminalColor).print();

		return identifier;
	}

	@Override
	public Node visitOperator(Operator op, Node parent) {
		Node operator = new Node(parent, op.toString());
		operator.setNodeLabel("\\\"" + op.spelling + "\\\" " + op.toString()).setNodeStyle(terminalColor).print();

		return operator;
	}

	@Override
	public Node visitIntLiteral(IntLiteral num, Node parent) {
		Node intLiteral = new Node(parent, num.toString());
		intLiteral.setNodeLabel("\\\"" + num.spelling + "\\\" " + num.toString()).setNodeStyle(terminalColor).print();

		return intLiteral;
	}

	@Override
	public Node visitBooleanLiteral(BooleanLiteral bool, Node parent) {
		Node booleanLiteral = new Node(parent, bool.toString());
		booleanLiteral.setNodeLabel("\\\"" + bool.spelling + "\\\" " + bool.toString()).setNodeStyle(terminalColor).print();

		return booleanLiteral;
	}  
}
