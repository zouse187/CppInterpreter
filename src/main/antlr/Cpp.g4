grammar Cpp;

// Parser
cexpr   : eform EOF ;

/*
eform   : term (('+' | '-') term)* ;
term    : primary (('*' | '/') primary)* ;
primary : INT
        | '(' eform ')'
        | '-' primary
        ;
*/

eform   : atom              #atomExpr
        | '-' atom          #unaryMinusExpr
        | eform '*' eform   #mulExpr
        | eform '/' eform   #divExpr
        | eform '+' eform   #addExpr
        | eform '-' eform   #subExpr
        ;

atom    : INT               #intLit
        | '(' eform ')'     #parenExpr
        ;

// Lexer
INT     : [0-9]+ ;

WS      : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;
