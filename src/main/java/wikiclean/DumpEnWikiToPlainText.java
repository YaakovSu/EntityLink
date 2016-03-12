package wikiclean;

import org.apache.commons.cli.*;

import java.io.PrintStream;

public class DumpEnWikiToPlainText {
  private static final String INPUT_OPTION = "input";

  @SuppressWarnings("static-access")
  public static void main(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("bz2 Wikipedia XML dump file").create(INPUT_OPTION));

    CommandLine cmdline = null;
    CommandLineParser parser = new GnuParser();
    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      System.exit(-1);
    }

    if (!cmdline.hasOption(INPUT_OPTION)) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(DumpEnWikiToPlainText.class.getCanonicalName(), options);
      System.exit(-1);
    }

    String path = cmdline.getOptionValue(INPUT_OPTION);
    PrintStream out = new PrintStream(System.out, true, "UTF-8");
    WikiClean cleaner = new WikiCleanBuilder()
                              .withLanguage(WikiClean.WikiLanguage.EN).withTitle(false)
                              .withFooter(false).build();

    WikipediaBz2DumpInputStream stream = new WikipediaBz2DumpInputStream(path);
    String page;
    while ((page = stream.readNext()) != null) {
      if ( page.contains("<ns>") && !page.contains("<ns>0</ns>")) {
        continue;
      }

      String s = cleaner.clean(page).replaceAll("\\n+", " ");
      if (s.startsWith("#REDIRECT")) {
        continue;
      }

      out.println(cleaner.getTitle(page).replaceAll("\\n+", " ") + "\t" + s);
    }
    out.close();
  }
}
