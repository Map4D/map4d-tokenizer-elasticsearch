package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.CocCocTokenizer;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.CocCocTokenizerPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.PluginInfo;
import org.elasticsearch.test.ESIntegTestCase;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;

import static org.apache.lucene.analysis.BaseTokenStreamTestCase.assertTokenStreamContents;
import static org.hamcrest.Matchers.*;

public class CocCocTokenizerPluginIntegrationTest extends ESIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singleton(CocCocTokenizerPlugin.class);
    }

    public void testLoadPlugin() throws Exception {
        final NodesInfoResponse response = client().admin().cluster().prepareNodesInfo().setPlugins(true).get();
        for (NodeInfo nodeInfo : response.getNodes()) {
            boolean pluginFound = false;
            for (PluginInfo pluginInfo : nodeInfo.getPlugins().getPluginInfos()) {
                if (pluginInfo.getName().equals(CocCocTokenizerPlugin.class.getName())) {
                    pluginFound = true;
                    break;
                }
            }
            assertThat(pluginFound, is(true));
        }
    }

    public void testCocCocTokenizerPlugin() throws IOException {
        TestAnalysis analysis = createTestAnalysis("default_coccoc_tokenizer.json");
        assertNotNull(analysis);

        TokenizerFactory tokenizerFactory = analysis.tokenizer.get("coccoc_tokenizer");
        assertNotNull(tokenizerFactory);
        assertThat(tokenizerFactory, instanceOf(CocCocTokenizerFactory.class));
    }

    public void testDefaultCocCocTokenizer() throws IOException {
        TestAnalysis analysis = createTestAnalysis("default_coccoc_tokenizer.json");
        assertNotNull(analysis);

        TokenizerFactory tokenizerFactory = analysis.tokenizer.get("coccoc_tokenizer");
        assertNotNull(tokenizerFactory);
        assertThat(tokenizerFactory, instanceOf(CocCocTokenizerFactory.class));

        CocCocTokenizerFactory cocCocTokenizerFactory = (CocCocTokenizerFactory)tokenizerFactory;
        CocCocTokenizer tokenizer = (CocCocTokenizer) cocCocTokenizerFactory.create();
        assertNotNull(tokenizer);

        tokenizer.setReader(new StringReader("Em út theo anh cả vào miền Nam"));
        assertTokenStreamContents(tokenizer, new String[]{"em út", "theo", "anh cả", "vào", "miền nam"});

        tokenizer.setReader(new StringReader("Em ut theo anh ca vao mien Nam"));
        assertTokenStreamContents(tokenizer, new String[]{"em ut", "theo", "anh ca", "vao", "mien nam"});

        tokenizer.setReader(new StringReader("mua hàng ở thegioididong"));
        assertTokenStreamContents(tokenizer, new String[]{"mua", "hàng", "ở", "thegioididong"});
    }

    private TestAnalysis createTestAnalysis(final String jsonSource) throws IOException {
        Settings settings = Settings.builder()
                .loadFromStream(jsonSource, CocCocTokenizerPluginIntegrationTest.class.getResourceAsStream(jsonSource), true)
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        Settings nodeSettings = Settings.builder().put(Environment.PATH_HOME_SETTING.getKey(), createTempDir()).build();
        return createTestAnalysis(new Index("test", "_na_"), nodeSettings, settings, new CocCocTokenizerPlugin());
    }

}
