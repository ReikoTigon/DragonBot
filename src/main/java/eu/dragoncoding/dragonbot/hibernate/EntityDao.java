package eu.dragoncoding.dragonbot.hibernate;

import com.mysql.cj.exceptions.CJCommunicationsException;
import eu.dragoncoding.dragonbot.structures.CriteriaPackage;
import eu.dragoncoding.dragonbot.structures.Entity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EntityDao {

    public static <T extends Object & Entity> void save(T objectToSave) {
        Logger logger = LoggerFactory.getLogger(objectToSave.getClass());
        boolean retry = false;
        int retries = 0;

        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                session.save(objectToSave);

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);
    }
    public static <T extends Object & Entity> void update(T objectToUpdate) {
        Logger logger = LoggerFactory.getLogger(objectToUpdate.getClass());
        boolean retry = false;
        int retries = 0;

        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                session.update(objectToUpdate);

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);
    }
    public static <T extends Object & Entity> void delete(T objectToDelete) {
        Logger logger = LoggerFactory.getLogger(objectToDelete.getClass());
        boolean retry = false;
        int retries = 0;

        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                session.delete(objectToDelete);

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);
    }
    public static <T extends Object & Entity> void delete(ArrayList<T> objectsToDelete) {
        Logger logger;
        if (!objectsToDelete.isEmpty()) {
            logger = LoggerFactory.getLogger(objectsToDelete.get(0).getClass());
        } else {
            logger = LoggerFactory.getLogger(EntityDao.class);
        }

        boolean retry = false;
        int retries = 0;

        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                objectsToDelete.forEach(session::delete);

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);
    }

    public static <T extends Object & Entity> T get(Class<T> clazz, int ID) {
        Logger logger = LoggerFactory.getLogger(clazz);
        boolean retry = false;
        int retries = 0;

        T result = null;
        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                CriteriaPackage<T> criteriaPackage = createCriteria(clazz);
                CriteriaBuilder cb = criteriaPackage.getCriteriaBuilder();
                CriteriaQuery<T> query = criteriaPackage.getQuery();
                Root<T> root = criteriaPackage.getRoot();

                query.select(root).where(cb.equal(root.get("id"), ID));

                result = session.createQuery(query)
                        .setMaxResults(1)
                        .getSingleResult();

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);

        return result;
    }
    public static <T extends Object & Entity> T get(Class<T> clazz, CriteriaQuery<T> query) {
        Logger logger = LoggerFactory.getLogger(clazz);
        boolean retry = false;
        int retries = 0;

        T result = null;
        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                result = session.createQuery(query)
                        .setMaxResults(1)
                        .getSingleResult();

                transaction.commit();

                logger.debug(result.toString());
            } catch (CJCommunicationsException e2) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (NoResultException e1) {
                logger.debug(e1.getMessage());
            }catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);

        return result;
    }
    public static <T extends Object & Entity> ArrayList<T> getAll(Class<T> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        boolean retry = false;
        int retries = 0;

        List<T> result = new ArrayList<>();
        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                result = session.createQuery(createCriteria(clazz).getQuery())
                        .getResultList();

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);

        return new ArrayList<>(result);
    }
    public static <T extends Object & Entity> ArrayList<T> getAllByQuery(Class<T> clazz, CriteriaQuery<T> query) {
        Logger logger = LoggerFactory.getLogger(clazz);
        boolean retry = false;
        int retries = 0;

        List<T> result = new ArrayList<>();
        Transaction transaction;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {
                transaction = session.beginTransaction();

                result = session.createQuery(query)
                        .getResultList();

                transaction.commit();
            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (JDBCConnectionException e2) {
                retry = true;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);

        return new ArrayList<>(result);
    }

    public static boolean reconnect(int timesRetried, Logger logger) {
        boolean retry = true;

        switch (timesRetried) {
            case 0 -> logger.error("Database timeout. First reconnect.");
            case 1 -> logger.error("Database timeout. Second reconnect.");
            case 2 -> logger.error("Database timeout. Third and last reconnect.");
            default -> {
                logger.error("Reconnect Failed.");
                retry = false;
            }
        }

        return retry;
    }
    public static <T extends Object & Entity> CriteriaPackage<T> createCriteria(Class<T> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        boolean retry = false;
        int retries = 0;

        CriteriaPackage<T> criteriaPackage = null;
        do {
            try (Session session = HibernateUtils.INSTANCE.getFactory().openSession()) {

                criteriaPackage = new CriteriaPackage<>(session.getCriteriaBuilder(), clazz);

            } catch (CJCommunicationsException e1) {
                retry = reconnect(retries, logger);

                retries++;
            } catch (Exception e) {
                retry = false;
                logger.error(e.getMessage(), e);
            }
        } while (retry);

        return criteriaPackage;
    }
}
