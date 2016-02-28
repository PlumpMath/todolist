(ns todolist.item.handler
  (:require [todolist.item.model :refer [create-item
                                         read-table
                                         read-column
                                         update-checked
                                         update-priority
                                         delete-item]]
            [todolist.item.view :refer [items-page]]))


(defn handle-index-items [req]
  (let [db (:webdev/db req)
        items (read-table db)]
    {:status 200
     :headers {}
     :body (items-page items)}))

(defn handle-create-item [req]
  (let [description (get-in req [:params "description"])
        db (:webdev/db req)
        item-id (create-item db description)]
    {:status 302
     :headers {"Location" "/"}
     :body "" }))

(defn handle-delete-item [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        exists? (delete-item db item-id)]
    (if exists?
      {:status 302
       :headers {"Location" "/"}
       :body ""}
      {:status 404
       :headers {}
       :body "List not found."})))

(defn handle-update [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        action (:action (:route-params req))
        checked (get-in req [:params "checked"])
        priority (get-in req [:params "priority"])]
    (case action
      "check" (let [exists? (update-checked db item-id (= "true" checked))]
                (if exists?
                  (do
                    (if (read-column db item-id "priority")
                      (update-priority db item-id false))
                    {:status 302
                     :headers {"Location" "/"}
                     :body ""})
                  {:status 404
                   :headers {}
                   :body "Item not found."}))
      "prior" (let [exists? (update-priority db item-id (= "true" priority))]
                (if exists?
                  (do
                    (if (read-column db item-id "checked")
                      (update-checked db item-id false))
                    {:status 302
                     :headers {"Location" "/"}
                     :body ""})
                  {:status 404
                   :headers {}
                   :body "Item not "})))))
