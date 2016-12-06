
package com.whcis.data.ap.newtemplate;

public class PenaltyWHBean {
    public static String id;// 用于数据传输发生错误时记录传输失败的数据

    public static String CF_WSH;// 行政处罚决定书文号

    public static String CF_CFMC;// 处罚名称

    public static String CF_CFLB1;// 处罚类别1

    public static String CF_CFLB2;// 处罚类别2

    public static String CF_SY;// 处罚事由

    public static String CF_YJ;// 处罚依据

    public static String CF_XDR_MC;// 行政相对人名称

    public static String CF_XDR_SHXYM;// 行政相对人代码_1(统一社会信用代码)

    public static String CF_XDR_ZDM;// 行政相对人代码_2(组织机构代码)

    public static String CF_XDR_GSDJ;// 行政相对人代码_3(工商登记码)

    public static String CF_XDR_SWDJ;// 行政相对人代码_4(税务登记号)

    public static String CF_XDR_SFZ;// 行政相对人代码_5(居民身份证号)

    public static String CF_FR;// 法定代表人姓名

    public static String CF_JG;// 处罚结果

    public static String CF_JDRQ;// 处罚决定日期

    public static String CF_XZJG;// 处罚机关

    public static String CF_ZT;// 当前状态

    public static String DFBM;// 地方编码

    public static String SJC;// 数据更新时间戳

    public static String BZ;// 备注

    public static void setX(int i, String contents) {
        contents = contents.replace("'", "");
        if (contents.equals("/") || contents.equals("") || contents.equals("无")) {
            contents = "null";
        } else {
            contents = "'" + contents + "'";
        }
        switch (i) {
            case 0:
                CF_WSH = contents;
                break;
            case 1:
                CF_CFMC = contents;
                break;
            case 2:
                if (contents.length() > 74) {
                    contents = "'" + contents.substring(1, contents.length() - 1).substring(0, 72) + "'";
                }
                CF_CFLB1 = contents;
                break;
            case 3:
                if (contents.length() > 74) {
                    contents = "'" + contents.substring(1, contents.length() - 1).substring(0, 72) + "'";
                }
                CF_CFLB2 = contents;
                break;
            case 4:
                CF_SY = contents;
                break;
            case 5:
                CF_YJ = contents;
                break;
            case 6:
                CF_XDR_MC = contents;
                break;
            case 7:
                CF_XDR_SHXYM = contents;
                break;
            case 8:
                CF_XDR_ZDM = contents;
                break;
            case 9:
                CF_XDR_GSDJ = contents;
                break;
            case 10:
                CF_XDR_SWDJ = contents;
                break;
            case 11:
                CF_XDR_SFZ = contents;
                break;
            case 12:
                CF_FR = contents;
                break;
            case 13:
                CF_JG = contents;
                break;
            case 14:
                if (contents.equals("null")) {
                    CF_JDRQ = contents;
                } else {
                    CF_JDRQ = Common.sToDate(contents.substring(1, contents.length() - 1));
                }
                break;
            case 15:
                CF_XZJG = contents;
                break;
            case 16:
                CF_ZT = Common.toState(contents.substring(1, contents.length() - 1));
                break;
            case 17:
                DFBM = contents;
                break;
            case 18:
                if (contents.equals("null")) {
                    SJC = contents;
                } else {
                    SJC = Common.sToDate(contents.substring(1, contents.length() - 1));
                }
                break;
            case 19:
                BZ = contents;
                break;
            default:
                break;
        }
    }

    public static String toValues() {
        String s = "";
        s = "(" + CF_WSH + "," + CF_CFMC + "," + CF_CFLB1 + "," + CF_CFLB2 + "," + CF_SY + "," + CF_YJ + "," + CF_XDR_MC + "," + CF_XDR_SHXYM + "," + CF_XDR_ZDM + "," + CF_XDR_GSDJ + ","
                + CF_XDR_SWDJ + "," + CF_XDR_SFZ + "," + CF_FR + "," + CF_JG + "," + CF_JDRQ + "," + CF_XZJG + "," + CF_ZT + "," + DFBM + "," + SJC + "," + BZ + ")";
        return s;
    }

    public static void clean() {
        CF_XDR_MC = CF_FR = CF_XDR_SHXYM = CF_XDR_ZDM = CF_XDR_GSDJ = CF_XDR_SWDJ = CF_XDR_SFZ = CF_CFLB1 = CF_WSH = CF_SY = CF_YJ = CF_JG = CF_JDRQ = CF_XZJG = CF_ZT = DFBM = SJC = BZ = CF_CFMC = null;
    }

    public static boolean isEmpty() {
        // TODO Auto-generated method stub
        return (CF_XDR_MC == null || CF_XDR_MC.equals("null")) && (CF_FR == null || CF_FR.equals("null"));
    }

    public static String toID() {
        String s = "";
        boolean isFrist = true;
        if (!CF_WSH.equals("null")) {
            s = s + " CF_WSH=" + CF_WSH;
            isFrist = false;
        }
        if (!CF_CFMC.equals("null")) {
            if (isFrist) {
                s = s + " CF_CFMC=" + CF_CFMC;
                isFrist = false;
            } else {
                s = s + " AND CF_CFMC=" + CF_CFMC;
            }
        }
        if (!CF_SY.equals("null")) {
            if (isFrist) {
                s = s + " CF_SY=" + CF_SY;
                isFrist = false;
            } else {
                s = s + " AND CF_SY=" + CF_SY;
            }
        }
        if (!CF_XDR_MC.equals("null")) {
            if (isFrist) {
                s = s + " CF_XDR_MC=" + CF_XDR_MC;
                isFrist = false;
            } else {
                s = s + " AND CF_XDR_MC=" + CF_XDR_MC;
            }
        }
        if (!CF_JG.equals("null")) {
            if (isFrist) {
                s = s + " CF_JG=" + CF_JG;
            } else {
                s = s + " AND CF_JG=" + CF_JG;
            }
        }
        return s;
    }

}
