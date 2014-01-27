package edu.macalester.comp124.sec01;

import acm.program.ConsoleProgram;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.core.cmd.Env;
import org.wikapidia.core.cmd.EnvBuilder;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.dao.LocalPageDao;
import org.wikapidia.core.dao.UniversalPageDao;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.core.model.LocalPage;
import org.wikapidia.core.model.UniversalPage;
import org.wikapidia.phrases.PhraseAnalyzer;

import java.util.Map;

/**
 * @author Shilad Sen
 */
public class WikAPIdiaDemo extends ConsoleProgram {
    private PhraseAnalyzer analyzer;
    private UniversalPageDao universalDao;
    private LocalPageDao localPageDao;

    @Override
    public void run() {
        try {
            Language simple = Language.getByLangCode("simple");
            Language sco = Language.getByLangCode("sco");
            Env env = new EnvBuilder().build();
            analyzer = env.getConfigurator().get(PhraseAnalyzer.class);
            universalDao = env.getConfigurator().get(UniversalPageDao.class);
            localPageDao = env.getConfigurator().get(LocalPageDao.class);


            while (true) {
                String phrase = readLine("enter phrase: ");
                Map<LocalPage, Float> senses = analyzer.resolve(simple, phrase, 20);
                for (LocalPage page : senses.keySet()) {
                    println("\tpage:" + page + " in scots is " + translate(page, sco));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public LocalPage translate(LocalPage page, Language to) throws DaoException {
        UniversalPage concept = universalDao.getByLocalPage(page, 1);
        if (concept.isInLanguage(to)) {
            LocalId id = concept.getLocalEntities(to).iterator().next();
            return localPageDao.getById(id.getLanguage(), id.getId());
        } else {
            return null;
        }
    }
}
