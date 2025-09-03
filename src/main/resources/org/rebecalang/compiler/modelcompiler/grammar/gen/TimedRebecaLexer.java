// Generated from /Users/mehdi/Desktop/org.rebecalang.compiler/src/main/resources/org/rebecalang/compiler/modelcompiler/grammar/TimedRebecaLexer.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TimedRebecaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MAILBOX=1, KNOWNSENDERS=2, ORDERS=3, MIN=4, MAX=5, NETWORK=6, KNOWNNODES=7, 
		DELAYS=8, LOSSES=9, CONNECT=10, AFTER=11, DEADLINE=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"MAILBOX", "KNOWNSENDERS", "ORDERS", "MIN", "MAX", "NETWORK", "KNOWNNODES", 
			"DELAYS", "LOSSES", "CONNECT", "AFTER", "DEADLINE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'mailbox'", "'knownsenders'", "'orders'", "'min'", "'max'", "'network'", 
			"'knownNodes'", "'delays'", "'losses'", "'->'", "'after'", "'deadline'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "MAILBOX", "KNOWNSENDERS", "ORDERS", "MIN", "MAX", "NETWORK", "KNOWNNODES", 
			"DELAYS", "LOSSES", "CONNECT", "AFTER", "DEADLINE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public TimedRebecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TimedRebecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\fp\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0000\u0000\f\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0001\u0000\u0000o\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0001\u0019"+
		"\u0001\u0000\u0000\u0000\u0003!\u0001\u0000\u0000\u0000\u0005.\u0001\u0000"+
		"\u0000\u0000\u00075\u0001\u0000\u0000\u0000\t9\u0001\u0000\u0000\u0000"+
		"\u000b=\u0001\u0000\u0000\u0000\rE\u0001\u0000\u0000\u0000\u000fP\u0001"+
		"\u0000\u0000\u0000\u0011W\u0001\u0000\u0000\u0000\u0013^\u0001\u0000\u0000"+
		"\u0000\u0015a\u0001\u0000\u0000\u0000\u0017g\u0001\u0000\u0000\u0000\u0019"+
		"\u001a\u0005m\u0000\u0000\u001a\u001b\u0005a\u0000\u0000\u001b\u001c\u0005"+
		"i\u0000\u0000\u001c\u001d\u0005l\u0000\u0000\u001d\u001e\u0005b\u0000"+
		"\u0000\u001e\u001f\u0005o\u0000\u0000\u001f \u0005x\u0000\u0000 \u0002"+
		"\u0001\u0000\u0000\u0000!\"\u0005k\u0000\u0000\"#\u0005n\u0000\u0000#"+
		"$\u0005o\u0000\u0000$%\u0005w\u0000\u0000%&\u0005n\u0000\u0000&\'\u0005"+
		"s\u0000\u0000\'(\u0005e\u0000\u0000()\u0005n\u0000\u0000)*\u0005d\u0000"+
		"\u0000*+\u0005e\u0000\u0000+,\u0005r\u0000\u0000,-\u0005s\u0000\u0000"+
		"-\u0004\u0001\u0000\u0000\u0000./\u0005o\u0000\u0000/0\u0005r\u0000\u0000"+
		"01\u0005d\u0000\u000012\u0005e\u0000\u000023\u0005r\u0000\u000034\u0005"+
		"s\u0000\u00004\u0006\u0001\u0000\u0000\u000056\u0005m\u0000\u000067\u0005"+
		"i\u0000\u000078\u0005n\u0000\u00008\b\u0001\u0000\u0000\u00009:\u0005"+
		"m\u0000\u0000:;\u0005a\u0000\u0000;<\u0005x\u0000\u0000<\n\u0001\u0000"+
		"\u0000\u0000=>\u0005n\u0000\u0000>?\u0005e\u0000\u0000?@\u0005t\u0000"+
		"\u0000@A\u0005w\u0000\u0000AB\u0005o\u0000\u0000BC\u0005r\u0000\u0000"+
		"CD\u0005k\u0000\u0000D\f\u0001\u0000\u0000\u0000EF\u0005k\u0000\u0000"+
		"FG\u0005n\u0000\u0000GH\u0005o\u0000\u0000HI\u0005w\u0000\u0000IJ\u0005"+
		"n\u0000\u0000JK\u0005N\u0000\u0000KL\u0005o\u0000\u0000LM\u0005d\u0000"+
		"\u0000MN\u0005e\u0000\u0000NO\u0005s\u0000\u0000O\u000e\u0001\u0000\u0000"+
		"\u0000PQ\u0005d\u0000\u0000QR\u0005e\u0000\u0000RS\u0005l\u0000\u0000"+
		"ST\u0005a\u0000\u0000TU\u0005y\u0000\u0000UV\u0005s\u0000\u0000V\u0010"+
		"\u0001\u0000\u0000\u0000WX\u0005l\u0000\u0000XY\u0005o\u0000\u0000YZ\u0005"+
		"s\u0000\u0000Z[\u0005s\u0000\u0000[\\\u0005e\u0000\u0000\\]\u0005s\u0000"+
		"\u0000]\u0012\u0001\u0000\u0000\u0000^_\u0005-\u0000\u0000_`\u0005>\u0000"+
		"\u0000`\u0014\u0001\u0000\u0000\u0000ab\u0005a\u0000\u0000bc\u0005f\u0000"+
		"\u0000cd\u0005t\u0000\u0000de\u0005e\u0000\u0000ef\u0005r\u0000\u0000"+
		"f\u0016\u0001\u0000\u0000\u0000gh\u0005d\u0000\u0000hi\u0005e\u0000\u0000"+
		"ij\u0005a\u0000\u0000jk\u0005d\u0000\u0000kl\u0005l\u0000\u0000lm\u0005"+
		"i\u0000\u0000mn\u0005n\u0000\u0000no\u0005e\u0000\u0000o\u0018\u0001\u0000"+
		"\u0000\u0000\u0001\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}