/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
  
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
        	case Token.PLUS:
            case Token.MINUS:
        		return 1;
        	case Token.MULT:
            case Token.DIV:
            case Token.MOD:
        		return 2;
            case Token.EXP:
                return 3;
            case Token.UNARY:
                return 4;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("suma " + a + " + " + b);
        	this.operandos.push(a + b);
        } else if(op.equals(Token.MINUS)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("minus " + b + " - " + a);
        	this.operandos.push(b -a);
        } else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mult " + a + " * " + b);
        	this.operandos.push(a * b);
        } else if(op.equals(Token.DIV)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("div " + b + " / " + a);
        	this.operandos.push(b / a);
        } else if(op.equals(Token.MOD)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mod " + a + " % " + b);
        	this.operandos.push(a % b);
        } else if(op.equals(Token.EXP)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("exp " + a + " ^ " + b);
        	this.operandos.push(Math.pow(a,b));
        } else if(op.equals(Token.UNARY)) {
            double a = operandos.pop();
            this.operandos.push(-a);
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
        	// Obtenemos la precedencia de quien ya estaba en el stack
        	// Comparamos las precedencias y decidimos si hay que operar
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack
    if (operadores.isEmpty()) {
        operadores.push(op);
        return;
    }

    while (!operadores.isEmpty() && pre(operadores.peek()) > pre(op)) {
        popOp();
    }

    operadores.push(op);
    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        return  T() && E1();
    }

    private boolean E1(){
        return  term(Token.PLUS) && T() && E1() ||
                term(Token.MINUS) && T() && E1() || true;

    }

    private boolean T() {
        return P() && T1();
    }

    private boolean T1() {
        return  term(Token.MULT) && P() && T1() ||
                term(Token.DIV) && P() && T1() ||
                term(Token.MOD) && P() && T1() || true;
    }

    private boolean P() {
        return F() && P1();
    }

    private boolean P1() {
        return term(Token.EXP) && F() && P1() || true;
    }

    private boolean F() {
        return  term(Token.UNARY) && F() ||
                term(Token.LPAREN) && E() && term(Token.RPAREN) ||
                term(Token.NUMBER);
    }
    /* TODO: sus otras funciones aqui */
}
