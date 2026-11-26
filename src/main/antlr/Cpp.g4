grammar Cpp;

// Parser
cexpr   : eform EOF ;

eform   : term (('+' | '-') term)* ;
term    : primary (('*' | '/') primary)* ;

primary : INT               #intLit
        | '(' eform ')'     #paren
        | '-' primary       #unaryMinus
        ;

/*
eform   : atom              #atomExpr
        | '-' atom          #unaryMinusExpr
        | eform '*' eform   #mulExpr
        | eform '/' eform   #divExpr
        | eform '+' eform   #addExpr            <- Geht nicht: 8/(4/2) - 2 + 5 = -3 ???
        | eform '-' eform   #subExpr                           8/(4/2) - 2 + 5 = 7 !!!
        ;

atom    : INT               #intLit
        | '(' eform ')'     #parenExpr
        ;
*/

// Lexer
INT     : [0-9]+ ;

WS      : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;
