package br.com.drummond.mycontacts;

public class Class_Operadora {

    public String NomearOperadora(String request){
        String ddd = request.substring(0, 2);

        switch (ddd) {
            case "99":
                ddd = "Erro";
                break;
            case "15":
                ddd = "Vivo";
                break;
            case "21":
                ddd = "Claro BR";
                break;
            case "14":
                ddd = "Oi";
                break;
            case "31":
                ddd = "Oi";
                break;
            case "41":
                ddd = "Tim";
                break;
            case "98":
                ddd = "Fixo";
                break;
            case "78":
                ddd = "Nextel";
                break;
            case "12":
                ddd = "CTBC";
                break;
            case "23":
                ddd = "Intelig-Tim";
                break;
            case "32":
                ddd = "Convergia";
                break;
            case "43":
                ddd = "Sercomtel";
                break;
            case "16":
                ddd = "Viacom";
                break;
            default:

                break;
        }
        return ddd;
    }
}
