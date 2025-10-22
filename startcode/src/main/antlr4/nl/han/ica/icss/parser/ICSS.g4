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
stylesheet: (variable_assignment| stylerule)*;
stylerule: selector OPEN_BRACE (if_clause | declaration)+ CLOSE_BRACE;
selector: ID_IDENT #id_selector | CLASS_IDENT #class_selector | HTML_IDENT #tag_selector;
declaration: property COLON value SEMICOLON;
property: LOWER_IDENT;
value: literal | variable_reference | operation;
variable_reference: CAPITAL_IDENT;
literal: bool #bool_literal | PIXELSIZE #pixel_literal | PERCENTAGE #percentage_literal | SCALAR #scalar_literal | COLOR #color_literal;
bool: TRUE | FALSE;
variable_assignment: variable_reference ASSIGNMENT_OPERATOR value SEMICOLON;
if_clause: IF BOX_BRACKET_OPEN (bool | variable_reference) BOX_BRACKET_CLOSE OPEN_BRACE body CLOSE_BRACE (else_clause)?;
body: (if_clause | declaration)*;
else_clause: ELSE OPEN_BRACE body CLOSE_BRACE;

operation:
    literal #operation_literal | variable_reference #opperation_value |
    operation MUL operation #multiply_operation |
    operation MIN operation #subtract_operation |
    operation PLUS operation #add_operation;

//operation: subtract_operation;
//subtract_operation: subtract_operation MIN add_operation | add_operation;
//add_operation: add_operation PLUS multiply_operation | multiply_operation;
//multiply_operation: multiply_operation MUL factor | factor;
//factor: literal | variable_reference;


