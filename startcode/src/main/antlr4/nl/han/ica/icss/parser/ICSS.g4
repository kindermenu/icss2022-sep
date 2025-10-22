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
stylerule: selector+ OPEN_BRACE (declaration+) CLOSE_BRACE | variable+;
selector: ID_IDENT #id_selector | CLASS_IDENT #class_selector | HTML_IDENT #tag_selector;
declaration: property COLON value SEMICOLON | if_clause;
property: LOWER_IDENT;
value: sum | literal | variable_value ;
variable_value: CAPITAL_IDENT | LOWER_IDENT;
literal: bool #bool_literal | PIXELSIZE #pixel_literal | PERCENTAGE #percentage_literal | SCALAR #scalar_literal | COLOR #color_literal;
bool: TRUE | FALSE;
variable: CAPITAL_IDENT ASSIGNMENT_OPERATOR value SEMICOLON;
if_clause: IF BOX_BRACKET_OPEN bool BOX_BRACKET_CLOSE OPEN_BRACE body CLOSE_BRACE (else_clause)?;
body: declaration+;
else_clause: ELSE OPEN_BRACE body CLOSE_BRACE;
sum: sum (PLUS | MIN | MUL) sum | literal;

