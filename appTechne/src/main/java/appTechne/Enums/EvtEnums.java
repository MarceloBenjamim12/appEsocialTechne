package appTechne.Enums;

import java.util.HashMap;
import java.util.Map;

public enum EvtEnums {
	evtInfoEmpregador("evtInfoEmpregador", 1),
	evtTabEstab("evtTabEstab", 1),
	evtTabRubrica("evtTabRubrica", 1),
	evtTabLotacao("evtTabLotacao", 1),
	evtTabCargo("evtTabCargo", 1),
	evtTabCarreira("evtTabCarreira", 1),
	evtTabFuncao("evtTabFuncao", 1),
	evtTabHorTur("evtTabHorTur", 1),
	evtTabAmbTrabalho("evtTabAmbTrabalho", 1),
	evtTabProcesso("evtTabProcesso", 1),
	evtTabOperPort("evtTabOperPort", 1),
	evtCadInicial("evtCadInicial", 1),
	evtAdmPrelim("evtAdmPrelim", 2),
	evtAdmissao("evtAdmissao", 2),
	evtAltCadastral("evtAltCadastral", 2),
	evtAltContratual("evtAltContratual", 2),
	evtCAT("evtCAT", 2),
	evtMonit("evtMonit", 2),
	evtAfastTemp("evtAfastTemp", 2),
	evtExpRisco("evtExpRisco", 2),
	evtInsApo("evtInsApo", 2),
	evtAvPrevio("evtAvPrevio", 2),
	evtReintegr("evtReintegr", 2),
	evtDeslig("evtDeslig", 2),
	evtTSVInicio("evtTSVInicio", 2),
	evtTSVAltContr("evtTSVAltContr", 2),
	evtTSVTermino("evtTSVTermino", 2),
	evtCdBenPrRP("evtCdBenPrRP", 2),
	evtRemun("evtRemun", 3),
	evtRemunRPPS("evtRemunRPPS", 3),
	evtBenPrRP("evtBenPrRP", 3),
	evtPgtos("evtPgtos", 3),
	evtAqProd("evtAqProd", 3),
	evtComProd("evtComProd", 3),
	evtContratAvNP("evtContratAvNP", 3),
	evtInfoComplPer("evtInfoComplPer", 3),
	evtContrSindPatr("evtContrSindPatr", 3);
	
	
	private String evt;
	private Integer grupo;
	private static Map<String, Integer> mapEvtSchema;
	
	EvtEnums(String evt, Integer grupo){
		this.evt = evt;
		this.grupo = grupo;
	}
	
	public static Integer getSchemaByEvt(String evt){
		if(mapEvtSchema == null){
			initializeMappingSchemaByEvt();
	      }
	      if(mapEvtSchema.containsKey(evt)){
	        return mapEvtSchema.get(evt);
	      }
	      return null;
	}
	
	private static void initializeMappingSchemaByEvt() {
		mapEvtSchema = new HashMap<String, Integer>();
        for (EvtEnums s : EvtEnums.values()) {
        	mapEvtSchema.put(s.evt, s.grupo);
        }
    }
	
	
}
