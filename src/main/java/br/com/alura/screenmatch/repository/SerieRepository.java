package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    //procurar pelo título ignorando se é maiúsculo ou minúsculo
    //retornando um Optional pois pode não encontrar uma serie, derived queries
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie); 

    //procurando pelo nome de ator com ignoreCase e maior do que x avaliação
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    // para achar com as melhores avaliações de forma decrecente 
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

    // @Query(value = "select * from series WHERE series.total_temporadas <= 5 AND series.avaliacao >= 7.5, nativeQuery = true)
    // native query utilizar JPQL diretamente com o comando SQL igual no SQL
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);
    
    //o ILike já dá um ignoreCase e consegue verificar por parte do título digitado
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    //buscar os top 5 episódios ordenando por avaliação de forma descendente com limite de 5 buscas
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    // verificando a partir de um ano especificado 
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
