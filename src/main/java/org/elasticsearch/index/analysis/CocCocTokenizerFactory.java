package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.CocCocTokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

public class CocCocTokenizerFactory extends AbstractTokenizerFactory {

    private final boolean forTransforming;

    private final int tokenizeOption;

    public CocCocTokenizerFactory(final IndexSettings indexSettings, final Environment environment,
                                  final String ignored, final Settings settings) {
        super(indexSettings, settings, "coccoc_tokenizer");
        forTransforming = settings.getAsBoolean("for_transforming", Boolean.FALSE);
        tokenizeOption = settings.getAsInt("tokenize_option", 0);
    }

    @Override
    public Tokenizer create() {
        return new CocCocTokenizer(forTransforming, tokenizeOption);
    }
}
