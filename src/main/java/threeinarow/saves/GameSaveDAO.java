package threeinarow.saves;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * DAO class for the {@link GameSave} entity.
 */
public class GameSaveDAO extends GenericJpaDao<GameSave> {

    public GameSaveDAO() {
        super(GameSave.class);
    }


    /**
     * Returns the entity instance with the specified id from the
     * database. The method returns an empty {@link Optional} object when
     * the instance does not exists.
     *
     * @param id the id to look for
     * @return an {@link Optional} object wrapping the entity instance with
     * the specified primary key
     */
    @Transactional
    public void deleteByID(Long id){
        GameSave game = entityManager.find(GameSave.class, id);
        if(game != null){
            entityManager.remove(game);
        }
    }
}
