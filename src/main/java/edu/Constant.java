package edu;

/**
 * Created by youngsu on 15-12-30.
 * 存储常用的量
 */
public class Constant {
    public static final String OUTDIR = "C:\\youngsu\\EntityLinkingGitData\\output\\";
    public static final String QCWEOUT = OUTDIR + "QC_WE\\";
    public static final String QCWEinKCL = QCWEOUT + "InKCL\\";
    public static final String QCWENoinKCL = QCWEOUT + "NoInKCL\\";
    public static final String QCWE_NAME = QCWENoinKCL + "name\\";
    public static final String QCWE_FEATURE = QCWENoinKCL + "feature\\";
    public static final String QCWE_PREDICT = QCWENoinKCL + "predict\\";
    public static final String QCWE_PREDICTNAME = QCWENoinKCL + "predictName\\";

    public static final String NAMETAILE = "_name.output";
    public static final String FEATURETAILE = "_feature.output";

    public static final String CRFMODEL = "C:\\youngsu\\EntityLinkFinal\\english-left3words\\english-left3words-distsim.tagger";
    public static final String RSVMMODEL = "C:\\youngsu\\libsvm-3.20\\windows\\noInlinkModel";

    public static final String GRAPHJSON = "C:\\youngsu\\EntityLinkFinal\\web\\data\\flare.json";

}
