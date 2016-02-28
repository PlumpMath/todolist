(ns todolist.item.model
  (:require [clojure.java.jdbc :as db]))

(defn create-table [db]
  (db/execute! db
               ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute! db
               ["CREATE TABLE IF NOT EXISTS items
                  (id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                   description TEXT NOT NULL,
                   checked BOOLEAN NOT NULL DEFAULT FALSE,
                   priority BOOLEAN NOT NULL DEFAULT FALSE,
                   date_created TIMESTAMPTZ NOT NULL DEFAULT now())"]))

(defn create-item [db description]
  (:id (first (db/query db
                        ["INSERT INTO items (description)
                           VALUES (?)
                           RETURNING id"
                         description]))))

(defn read-table [db]
  (db/query db
            ["SELECT id, description, checked, priority, date_created
              FROM items
              ORDER BY checked, date_created"]))

(defn read-column [db id column]
  (db/query db
            ["SELECT ? FROM items
              WHERE id = ?"
             column
             id]))

(defn update-checked [db id checked]
  (= [1] (db/execute! db
                     ["UPDATE items
                      SET checked = ?
                       WHERE id = ?"
                      checked
                      id])))

(defn update-priority [db id priority]
  (= [1] (db/execute! db
                     ["UPDATE items
                      SET priority = ?
                      WHERE id = ?"
                      priority
                      id])))

(defn delete-item [db id]
  (= [1] (db/execute! db
          ["DELETE FROM items
            WHERE id = ?"
           id])))
