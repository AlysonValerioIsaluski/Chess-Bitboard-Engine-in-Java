Chess Bitboard Engine In Java

Este projeto é um motor de xadrez desenvolvido em Java, focado em alta performance e eficiência de memória. A implementação utiliza a técnica de **Bitboards** para representação do tabuleiro e geração de movimentos, permitindo que operações complexas sejam resolvidas através de manipulação direta de bits (bitwise operations).


> Detalhes Técnicos

* **Representação por Bitboards:** O estado do tabuleiro é armazenado em primitivas `long` de 64 bits. Cada bit representa uma casa, permitindo o uso de operações como `AND`, `OR`, `XOR` e `Shifts` para calcular movimentos possíveis de forma extremamente rápida.
* **Eficiência de Memória:** Ao minimizar a criação de objetos e focar em tipos primitivos, a engine reduz a pressão sobre o Garbage Collector, garantindo latência mínima e alta performance em cálculos de busca profunda.
* **Regras Completas (FIDE):**
  * Movimentação legal de todas as peças.
  * Roque (Castling) e Captura En Passant.
  * Sistema de Promoção de Peão com interface de escolha.
  * Detecção de Check, Checkmate e Empate (Stalemate/Material Insuficiente).

> Arquitetura / UI

* **Separação de Preocupações:** Lógica de negócio (Bitboard) isolada da camada de visualização (Swing).
* **Layered UI:** Uso de `JLayeredPane` para exibir menus de promoção e mensagens de fim de jogo sem interromper o fluxo de renderização do tabuleiro centralizado.
* **Otimização para IA:** O motor foi estruturado para servir de base para algoritmos de busca (Minimax/Alpha-Beta), onde a velocidade de geração de nós por segundo (NPS) é o fator crítico.

> Tecnologias:
  * Java 17+ e Java Swing para a Interface Gráfica
