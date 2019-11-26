package com.company.regras;

import com.company.TipoToken;
import com.company.TokenLexema;

import java.util.List;


/**
 * Created by usuario on 03/09/2019.
 */
public class Pmorto extends RegraAbstrata{


  @Override
  public void reconher(List<TokenLexema> lista, int indice) {
    System.out.println("Regra Ponto Morto");

    if(indice < lista.size()) {
      TokenLexema tl = lista.get(indice);
      //System.out.println(tl.getTipoToken());
      if(!TipoToken.PMORTO.equals(tl.getTipoToken())) {
        throw new TokenNaoReconhecidoException(tl);
      }

      indice ++;
      tl = lista.get(indice);
      if (TipoToken.EMBREAGEM.equals(tl.getTipoToken())) {
        Embreagem e = new Embreagem();
        e.reconher(lista, indice);
      } else {
        Desligar d = new Desligar();
        d.reconher(lista, indice);
      }
    }
  }
}
