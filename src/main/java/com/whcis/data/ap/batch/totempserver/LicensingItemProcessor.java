package com.whcis.data.ap.batch.totempserver;

import com.whcis.data.ap.model.LicensingTemp;
import com.whcis.data.ap.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

/**
 * Created by neo on 2017/6/29.
 */
public class LicensingItemProcessor implements ItemProcessor<LicensingTemp, LicensingTemp> {

    private static final Logger log = LoggerFactory.getLogger(LicensingItemProcessor.class);

    @Override
    public LicensingTemp process(final LicensingTemp licensing) throws Exception {

        final String xkWsh = licensing.getXkWsh();
        final String xkXmmc = licensing.getXkXmmc();
        final String xkSplb = licensing.getXkSplb();
        final String xkNr = licensing.getXkNr();
        final String xkXdr = licensing.getXkXdr();
        final String xkXdrShxym = licensing.getXkXdrShxym();
        final String xkXdrZdm = licensing.getXkXdrZdm();
        final String xkXdrGsdj = licensing.getXkXdrGsdj();
        final String xkXdrSwdj = licensing.getXkXdrSwdj();
        final String xkXdrSfz = licensing.getXkXdrSfz();
        final String xkFr = licensing.getXkFr();
        final Date xkJdrq = licensing.getXkJdrq();
        final Date xkJzq = licensing.getXkJzq();
        final String xkXzjg = licensing.getXkXzjg();
        final String xkZt = licensing.getXkZt();
        final String dfbm = licensing.getDfbm();
        final Date sjc = licensing.getSjc();
        final String bz = licensing.getBz();
        final String qtxx = licensing.getQtxx();
        final String sjmc = licensing.getSjmc();

        final LicensingTemp transformedLicensing = new LicensingTemp(xkWsh,
                                                                        xkXmmc,
                                                                        xkSplb,
                                                                        xkNr,
                                                                        xkXdr,
                                                                        xkXdrShxym,
                                                                        xkXdrZdm,
                                                                        xkXdrGsdj,
                                                                        xkXdrSwdj,
                                                                        xkXdrSfz,
                                                                        xkFr,
                                                                        xkJdrq,
                                                                        xkJzq,
                                                                        xkXzjg,
                                                                        xkZt,
                                                                        dfbm,
                                                                        sjc,
                                                                        bz,
                                                                        qtxx,
                                                                        sjmc);
        return transformedLicensing;
    }
}
