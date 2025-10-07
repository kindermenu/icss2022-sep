grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
LITERAL: TRUE | FALSE | PIXELSIZE | PERCENTAGE | SCALAR | COLOR;
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

HTML_IDENT: LINK | PARAGRAPH;
LINK: 'a';
PARAGRAPH: 'p';

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---
stylesheet: variabels stylerule*;
stylerule: selector+ OPEN_BRACE (decleration+) CLOSE_BRACE;
selector: ID_IDENT | CLASS_IDENT | HTML_IDENT;
decleration: property COLON value SEMICOLON | if_expression;
property: LOWER_IDENT;
value: LITERAL | CAPITAL_IDENT;
variabels: variabel*;
variabel: CAPITAL_IDENT ASSIGNMENT_OPERATOR LITERAL SEMICOLON;
if_expression: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE body CLOSE_BRACE (else_expression)?;
expression: CAPITAL_IDENT;
body: decleration+;
else_expression: ELSE OPEN_BRACE body CLOSE_BRACE;

// demo //

//stylesheet: stylerule;
//stylerule: id_selector OPEN_BRACE decleration CLOSE_BRACE;
//id_selector: ID_IDENT;
//decleration: property COLON pixel_literal SEMICOLON;
//property: LOWER_IDENT;
//pixel_literal: PIXELSIZE;

