grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
//LITERAL: TRUE | FALSE | PIXELSIZE | PERCENTAGE | SCALAR | COLOR;
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
stylesheet: stylerule*;
stylerule: selector+ OPEN_BRACE (declaration+) CLOSE_BRACE | variabel+;
selector: ID_IDENT #id_selector | CLASS_IDENT #class_selector | HTML_IDENT #tag_selector;
declaration: property COLON value SEMICOLON | if_expression;
property: LOWER_IDENT;
value: literal | CAPITAL_IDENT;
literal: bool #bool_literal | PIXELSIZE #pixel_literal | PERCENTAGE #percentage_literal | SCALAR #scalar_literal | COLOR #color_literal;
bool: TRUE | FALSE;
variabel: CAPITAL_IDENT ASSIGNMENT_OPERATOR value SEMICOLON;
if_expression: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE body CLOSE_BRACE (else_expression)?;
expression: CAPITAL_IDENT;
body: declaration+;
else_expression: ELSE OPEN_BRACE body CLOSE_BRACE;
//som:
//    PIXELSIZE #pixel_literal |
//    expression PLUS expression #addOperation;

// demo //

//stylesheet: stylerule;
//stylerule: id_selector OPEN_BRACE declaration CLOSE_BRACE;
//id_selector: ID_IDENT;
//declaration: property COLON pixel_literal SEMICOLON;
//property: LOWER_IDENT;
//pixel_literal: PIXELSIZE;

